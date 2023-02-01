package com.shopme.common.setting_bag;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingBag;

import java.util.List;

public class EmailSettingBag extends SettingBag {

    public EmailSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    private String getEmailSettingValue(EmailSettingType settingType) {
        return super.getValue(settingType.toString());
    }

    public String getHost() {
        return getEmailSettingValue(EmailSettingType.MAIL_HOST);
    }

    public String getPort() {
        return getEmailSettingValue(EmailSettingType.MAIL_PORT);
    }

    public String getUsername() {
        return getEmailSettingValue(EmailSettingType.MAIL_USERNAME);
    }

    public String getPassword() {
        return getEmailSettingValue(EmailSettingType.MAIL_PASSWORD);
    }

    public String getSmtpAuth() {
        return getEmailSettingValue(EmailSettingType.SMTP_AUTH);
    }

    public String getSmtpSecured() {
        return getEmailSettingValue(EmailSettingType.SMTP_SECURED);
    }

    public String getFormAddress() {
        return getEmailSettingValue(EmailSettingType.MAIL_FROM);
    }

    public String getSenderName() {
        return getEmailSettingValue(EmailSettingType.MAIL_SENDER_NAME);
    }

    public String getCustomerVerifySubject() {
        return getEmailSettingValue(EmailSettingType.CUSTOMER_VERIFY_SUBJECT);
    }

    public String getCustomerVerifyContent() {
        return getEmailSettingValue(EmailSettingType.CUSTOMER_VERIFY_CONTENT);
    }

    public String getOrderConfirmationSubject() {
        return super.getValue("ORDER_CONFIRMATION_SUBJECT");
    }

    public String getOrderConfirmationContent() {
        return super.getValue("ORDER_CONFIRMATION_CONTENT");
    }
}
