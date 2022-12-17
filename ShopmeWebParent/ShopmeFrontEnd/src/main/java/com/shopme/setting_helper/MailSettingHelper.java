package com.shopme.setting_helper;

import com.shopme.common.setting_bag.CurrencySettingBag;
import com.shopme.common.setting_bag.EmailSettingBag;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MailSettingHelper {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();

        return siteURL.replace(request.getServletPath(), "");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings) {
        return null;
    }

    public static String formatCurrency(float amount, CurrencySettingBag settingBag) {
        String symbol = settingBag.getSymbol();
        String symbolPosition = settingBag.getSymbolPosition();
        String decimalPointType = settingBag.getDecimalPointType();
        String thousandPointType = settingBag.getThousandPointType();
        int decimalDigits = settingBag.getDecimalDigits();

        String pattern = symbolPosition.equals("Before price") ? symbol : "";
        pattern += "###,###";

        if (decimalDigits > 0) {
            pattern += ".";
            for (int count = 1; count <= decimalDigits; count++)
                pattern += "#";
        }

        pattern += symbolPosition.equals("After price") ? symbol : "" ;
        char thousandSeparator = thousandPointType.equals("POINT") ? '.' : ',';
        char decimalSeparator = decimalPointType.equals("POINT") ? '.' : ',';

        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator(decimalSeparator);
        decimalFormatSymbols.setGroupingSeparator(thousandSeparator);

        DecimalFormat format = new DecimalFormat(pattern, decimalFormatSymbols);

        return format.format(amount);
    }
}
