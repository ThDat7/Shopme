package com.shopme.admin.controller;

import com.shopme.admin.service.MasterOrderReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    MasterOrderReportService reportService;

    @GetMapping("/sale_by_date/{period}")
    public ResponseEntity<?> getReportDataByPeriod(@PathVariable("period") String period) {
        return ResponseEntity.ok(reportService.getReportDataByPeriod(period));
    }

    @GetMapping("/sale_by_date/{startTime}/{endTime}")
    public ResponseEntity<?> getReportByDateRange(@PathVariable("startTime") String startDate,
                                                  @PathVariable("endTime") String endDate) throws ParseException {
        return ResponseEntity.ok(reportService.getReportDatByDateRange(startDate, endDate));
    }
}
