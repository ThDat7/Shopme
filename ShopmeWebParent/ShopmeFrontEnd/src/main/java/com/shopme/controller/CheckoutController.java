package com.shopme.controller;

import com.shopme.common.entity.Order;
import com.shopme.common.entity.PaymentMethod;
import com.shopme.common.entity.Setting;
import com.shopme.common.setting_bag.CurrencySettingBag;
import com.shopme.common.setting_bag.EmailSettingBag;
import com.shopme.common.setting_bag.PaymentSettingBag;
import com.shopme.payload.request.OrderDTO;
import com.shopme.payload.response.CheckoutInfo;
import com.shopme.security.JwtService;
import com.shopme.service.*;
import com.shopme.setting_helper.MailSettingHelper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SettingService settingService;

    @Autowired
    private PaypalService paypalService;


    @GetMapping
    public ResponseEntity<?> getCheckout(HttpServletRequest request) {
        int customerId = getCustomerId(request);

        CheckoutInfo checkoutInfo = checkoutService
                .prepareCheckout(customerId);

        return ResponseEntity.ok(checkoutInfo);
    }

    private int getCustomerId(HttpServletRequest request) {
        String token = request.getHeader(JwtService.HEADER);
        return jwtService.getCustomerId(token);
    }

    @PostMapping("/place-order")
    @ResponseStatus(HttpStatus.OK)
    public void placeOrder(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        int customerId = getCustomerId(request);

        String paymentType = request.getParameter("paymentMethod");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

        Order order = orderService.createOrder(customerId, paymentMethod);
        shoppingCartService.deleteAllByCustomer(customerId);
        sendOrderConfirmationEmail(request, order);
    }

    private void sendOrderConfirmationEmail(HttpServletRequest request, Order order) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = new EmailSettingBag(settingService.getEmailSettings());
        JavaMailSenderImpl mailSender = MailSettingHelper.prepareMailSender(emailSettingBag);
        mailSender.setDefaultEncoding("utf-8");

        String toAddress = order.getCustomer().getEmail();
        String subject = emailSettingBag.getOrderConfirmationSubject();
        String content = emailSettingBag.getOrderConfirmationContent();

        subject.replace("[[orderId]]", String.valueOf(order.getId()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setSubject(subject);
        helper.setTo(toAddress);
        helper.setFrom(emailSettingBag.getFormAddress(), emailSettingBag.getSenderName());

        String orderTime = new SimpleDateFormat().format(order.getOrderTime());

        CurrencySettingBag currencySetting = new CurrencySettingBag(settingService.getCurrencySettings());
        String totalAmount = MailSettingHelper.formatCurrency(order.getTotal(), currencySetting);

        content.replace("[[name]]", order.getCustomer().getFirstName());
        content.replace("[[orderId]]", String.valueOf(order.getId()));
        content.replace("[[orderTime]]", orderTime);
        content.replace("[[shippingAddress]]", order.getCountry()); //Shipping address
        content.replace("[[total]]", totalAmount);
        content.replace("[[paymentMethod]]", order.getPaymentMethod().toString());

        helper.setText(content);
        mailSender.send(message);
    }

    @GetMapping("/paypal_id")
    public ResponseEntity<?> getPaypalId() {
        PaymentSettingBag settingBag =
                new PaymentSettingBag(settingService.getPaymentSettings());

        return ResponseEntity.ok(settingBag.getClientId());
    }

    @GetMapping("/currency_code")
    public ResponseEntity<?> getCurrencyCode() {
        return ResponseEntity.ok(settingService.getCurrencyCode());
    }

    @PostMapping("/proccess_paypal_order")
    @ResponseStatus(HttpStatus.OK)
    public void processPaypalOrder(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String orderId = request.getParameter("orderId");
        paypalService.validateOrder(orderId);
        placeOrder(request);
    }

    private Order covertToEntity(OrderDTO orderDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(orderDTO, Order.class);
    }
}
