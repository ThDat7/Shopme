package com.shopme.admin.service.report;

import com.shopme.admin.payload.response.ReportItem;
import com.shopme.admin.payload.response.ReportType;
import com.shopme.common.entity.Order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService {
    protected DateFormat dateFormatter;

    public List<ReportItem> getReportDataLast7Days(ReportType reportType) {
        return getReportDataLastXDays(7, reportType);
    }

    public List<ReportItem> getReportDataLast28Days(ReportType reportType) {
        return getReportDataLastXDays(28, reportType);
    }

    public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
        return getReportDataLastXMonth(6, reportType);
    }

    public List<ReportItem> getReportDataLastYear(ReportType reportType) {
        return getReportDataLastXMonth(12, reportType);
    }

    public List<ReportItem> getReportDataLastXDays(int days, ReportType reportType) {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,  (-days + 1));

        Date startTime = cal.getTime();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    protected List<ReportItem> getReportDataLastXMonth(int months, ReportType reportType) {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,  (-months + 1));

        Date startTime = cal.getTime();
        dateFormatter = new SimpleDateFormat("yyyy-MM");
        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    public List<ReportItem> getReportDataByDateRange(String startTimeString, String endTimeString, ReportType reportType) {
        Date startTime;
        Date endTime;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startTime = dateFormatter.parse(startTimeString);
            endTime = dateFormatter.parse(endTimeString);
        } catch(ParseException e) {throw new IllegalArgumentException();}

        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    protected abstract List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType);
}
