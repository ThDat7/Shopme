package com.shopme.admin.controller;

import com.shopme.admin.service.CurrencyService;
import com.shopme.admin.service.SettingService;
import com.shopme.common.entity.Setting;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping()
public class SettingController {
    private SettingService settingService;

    private CurrencyService currencyService;

    public SettingController(SettingService settingService, CurrencyService currencyService) {
        this.settingService = settingService;
        this.currencyService = currencyService;
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(settingService.getAll());
    }

    @GetMapping("/currencies")
    public ResponseEntity<?> getCurrency() {
        return ResponseEntity.ok(currencyService.getAll());
    }

    @PostMapping("/settings/save-general")
    @ResponseStatus(HttpStatus.OK)
    public void save(@RequestParam(name = "fileImage", required = false) MultipartFile multipartFile) {

    }

    @PostMapping("/save_mail_sever")
    @ResponseStatus(HttpStatus.OK)
    public void saveMailServer(HashMap<String, String> settingByClient) {
        List<Setting> mailServerSettings =  settingService.getMailServerSettings();
        updateSettingValueFromForm(settingByClient, mailServerSettings);
    }

    @PostMapping("/save_mail_templates")
    @ResponseStatus(HttpStatus.OK)
    public void saveMailTemplates(HashMap<String, String> settingByClient) {
        List<Setting> mailTemplateSettings =  settingService.getMailTemplatesSettings();
        updateSettingValueFromForm(settingByClient, mailTemplateSettings);
    }

    @PostMapping("/settings/save-payment")
    @ResponseStatus(HttpStatus.OK)
    public void savePaymentSettings(HashMap<String, String> settingByClient) {
        List<Setting> paymentSettings = settingService.getPaymentSettings();
        updateSettingValueFromForm(settingByClient, paymentSettings);
    }


    private void updateSettingValueFromForm(HashMap<String, String> settingByClient,
                                            List<Setting> settings) {
        for (Setting setting : settings) {
            String value = settingByClient.get(setting.getKey());

            if (value != null) setting.setValue(value);
        }

        settingService.saveAll(settings);
    }


}
