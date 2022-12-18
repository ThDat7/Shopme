package com.shopme.service;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.CurrencyRepository;
import com.shopme.repository.SettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Setting> getGeneralSettings() {
        return settingRepository.findByTwoCategories(SettingCategory.GENERAL,
                SettingCategory.CURRENCY);
    }

    public List<Setting> getEmailSettings() {
        List<Setting> emailSettings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);

        emailSettings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

        return emailSettings;
    }

    public List<Setting> getCurrencySettings() {
        return settingRepository.findByCategory(SettingCategory.CURRENCY);
    }

    public String getCurrencyCode() {
        Setting currencySetting = settingRepository.findByKey("CURRENCY_ID")
                .orElseThrow(ResourceNotFoundException::new);
        int currencyId = Integer.valueOf(currencySetting.getValue());

        return currencyRepository.findById(currencyId)
                .orElseThrow(ResourceNotFoundException::new).getCode();
    }

    public List<Setting> getPaymentSettings() {
        return settingRepository.findByCategory(SettingCategory.PAYMENT);
    }
}
