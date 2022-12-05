package com.shopme.setting_helper;

import com.shopme.common.setting_bag.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.servlet.http.HttpServletRequest;

public class MailSettingHelper {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();

        return siteURL.replace(request.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
        return null;
    }

}
