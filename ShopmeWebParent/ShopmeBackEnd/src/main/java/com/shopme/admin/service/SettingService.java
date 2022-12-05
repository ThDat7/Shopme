package com.shopme.admin.service;

import com.shopme.admin.repository.SettingRepository;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import com.shopme.common.setting_bag.EmailSettingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;

    public List<Setting> getAll() {
        return settingRepository.findAll();
    }

    public List<Setting> getMailServerSettings() {
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplatesSettings() {
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public void saveAll(List<Setting> settings) {
        settingRepository.saveAll(settings);
    }

    public EmailSettingBag getEmailSettings() {
        List<Setting> emailSettings = settingRepository.findByCategory(
                SettingCategory.MAIL_SERVER);

        emailSettings.addAll(settingRepository.findByCategory(
                SettingCategory.MAIL_TEMPLATES));

        return new EmailSettingBag(emailSettings);
    }
}
