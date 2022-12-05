package com.shopme.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.setting_bag.EmailSettingBag;
import com.shopme.service.CustomerService;
import com.shopme.service.SettingService;
import com.shopme.setting_helper.MailSettingHelper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private SettingService settingService;
    private CustomerService customerService;

    public CustomerController(SettingService settingService, CustomerService customerService) {
        this.settingService = settingService;
        this.customerService = customerService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public void create(Customer customer) {
        customerService.save(customer);
    }

    @PostMapping("/check_email")
    @ResponseStatus(HttpStatus.OK)
    public void checkEmailUnique(@RequestParam("email") String email) {
        customerService.valueEmailUnique(email);
    }

    @PostMapping("/create_customer")
    public void createCustomer(Customer customer, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        customerService.save(customer);
        sendVerificationEmail(request, customer);
    }

    @GetMapping("verify")
    @ResponseStatus(HttpStatus.OK)
    public void verifyAccount(@RequestParam("code")
                              String verificationCode) {
        customerService.verify(verificationCode);
    }

    private void sendVerificationEmail(HttpServletRequest request, Customer customer) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSetting = new EmailSettingBag(settingService.getEmailSettings());
        JavaMailSenderImpl mailSender = MailSettingHelper.prepareMailSender(emailSetting);

        String toAddress = customer.getEmail();
        String subject = emailSetting.getCustomerVerifySubject();
        String content = emailSetting.getCustomerVerifyContent();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSetting.getFormAddress(),
                emailSetting.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        String fullName = customer.getFirstName() + " " + customer.getLastName();

        content.replace("[[name]]", fullName);
        String verifyURL = MailSettingHelper.getSiteURL(request)
                + "/verify?code=" + customer.getVerificationCode();
        content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

}
