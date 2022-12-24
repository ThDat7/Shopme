package com.shopme.admin.controller;

import com.shopme.admin.payload.response.ReportType;
import com.shopme.admin.service.report.MasterOrderReportService;
import com.shopme.admin.service.SettingService;
import com.shopme.admin.service.report.OrderDetailReportService;
import com.shopme.common.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    private MasterOrderReportService masterOrderReportService;

    @Autowired
    private OrderDetailReportService orderDetailReportService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/sale_by_date/{period}")
    public ResponseEntity<?> getReportDataByPeriod(@PathVariable("period") String period) {
        return ResponseEntity.ok(masterOrderReportService.getReportDataByPeriod(period));
    }

    @GetMapping("/sale_by_date/{startTime}/{endTime}")
    public ResponseEntity<?> getReportByDateRange(@PathVariable("startTime") String startDate,
                                                  @PathVariable("endTime") String endDate) throws ParseException {
        return ResponseEntity.ok(masterOrderReportService.getReportDataByDateRange(startDate, endDate, ReportType.DAY));
    }

    @GetMapping("/get_setting")
    @ResponseStatus(HttpStatus.OK)
    public void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySetting = settingService.getCurrencySetting();

        for (Setting setting : currencySetting)
            request.setAttribute(setting.getKey(), setting.getValue());
    }

    @GetMapping("{groupBy}/{period}")
    public ResponseEntity<?> getReportDataByCategoryOrProduct(@PathVariable("groupBy")
                                                              String groupBy,
                                                              @PathVariable("period")
                                                              String period) {
        ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());

        return ResponseEntity.ok(
                orderDetailReportService.getReportDataByPeriod(reportType, period)
        );
    }

    @GetMapping("/{groupBy}/{startDate}/{endDate}")
    public ResponseEntity<?> getReportDataByCategoryOrProductDateRange(@PathVariable("groupBy")
                                                                       String groupBy,
                                                                       @PathVariable("startDate")
                                                                       String startDate,
                                                                       @PathVariable("endDate")
                                                                       String endDate) {
        ReportType reportType = ReportType.valueOf(groupBy.toUpperCase());

        return ResponseEntity.ok(
                orderDetailReportService.getReportDataByDateRange(startDate, endDate, reportType)
        );
    }

}
