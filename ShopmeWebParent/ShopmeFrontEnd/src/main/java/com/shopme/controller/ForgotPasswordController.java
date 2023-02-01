package com.shopme.controller;

import com.shopme.common.setting_bag.EmailSettingBag;
import com.shopme.service.CustomerService;
import com.shopme.service.SettingService;
import com.shopme.setting_helper.MailSettingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/forgot_password")
public class ForgotPasswordController {
    @Autowired
    private SettingService settingService;

    @Autowired
    private CustomerService customerService;

    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    public void forgotPassword(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");

        String token = customerService.updateResetPasswordToken(email);
        String link = MailSettingHelper.getSiteURL(request) + "/reset_password?token="
                + token;

        sendMail(link, email);

    }

    private void sendMail(String link, String email) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettings = new EmailSettingBag(
                settingService.getEmailSettings());
        JavaMailSenderImpl mailSender = MailSettingHelper.prepareMailSender(emailSettings);

        String toAddress = email;
        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "Click the link below to change your password:</p>"
                + "<p><a href=\"" + link + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFormAddress(),
                emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    @ResponseStatus(HttpStatus.OK)
    public void showResetPassword(@RequestParam("token") String token) {
        customerService.validateResetPasswordToken(token);
    }

    @PostMapping("/reset_password")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(HttpServletRequest request) {
        customerService.resetPassword(request);
    }
}
