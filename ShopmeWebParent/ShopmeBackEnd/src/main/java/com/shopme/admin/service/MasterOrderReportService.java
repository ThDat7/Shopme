package com.shopme.admin.service;

import com.shopme.admin.payload.response.ReportItem;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.common.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MasterOrderReportService {
    private DateFormat dateFormatter;

    @Autowired
    private OrderRepository orderRepository;

    public List<ReportItem> getReportDatByDateRange(String startTimeString, String endTimeString)
            throws ParseException {
        Date startTime;
        Date endTime;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
         try {
             startTime = dateFormatter.parse(startTimeString);
            endTime = dateFormatter.parse(endTimeString);
        } catch(ParseException e) {throw new IllegalArgumentException();}

        return getReportDayByDateRange(startTime, endTime, "days");
    }

    public List<ReportItem> getReportDataByPeriod(String period) {
        switch (period) {
            case "last_7_days": return getReportDataLast7Days();
            case "last_28_days": return getReportDataLast28Days();
            case "last_6_months": return getReportDataLast6Months();
            case "last_year": return getReportDataLastYear();

            default: return getReportDataLast7Days();
        }
    }

    public List<ReportItem> getReportDataLast6Months() {
        return getReportDataLastXMonth(6);
    }

    public List<ReportItem> getReportDataLastYear() {
        return getReportDataLastXMonth(12);
    }

    public List<ReportItem> getReportDataLast28Days() {
        return getReportDataLastXDays(28);
    }

    public List<ReportItem> getReportDataLast7Days() {
        return getReportDataLastXDays(7);
    }

    private List<ReportItem> getReportDataLastXDays(int days) {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH,  (-days + 1));

        Date startTime = cal.getTime();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        return getReportDayByDateRange(startTime, endTime, "days");
    }

    private List<ReportItem> getReportDataLastXMonth(int months) {
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,  (-months + 1));

        Date startTime = cal.getTime();
        dateFormatter = new SimpleDateFormat("yyyy-MM");
        return getReportDayByDateRange(startTime, endTime, "months");
    }

    private List<ReportItem> getReportDayByDateRange(Date startTime, Date endTime,
                                                     String period) {
        List<Order> orders = orderRepository.findByOrderTimeBetween(startTime, endTime);

        List<ReportItem> reportItems = createReportData(startTime, endTime, period);
        calculateSalesForReportData(orders, reportItems);
        return reportItems;
    }

    private List<ReportItem> createReportData(Date startTime, Date endTime,
                                              String period) {
        List<ReportItem> reportItems = new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startTime);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endTime);

        Date currentDate = startDate.getTime();
        String dateString = dateFormatter.format(currentDate);

        reportItems.add(new ReportItem(dateString));

        do {
            if (period.equals("days")) startDate.add(Calendar.DAY_OF_MONTH, 1);
            else if (period.equals("months")) startDate.add(Calendar.MONTH, 1);
            currentDate = startDate.getTime();
            dateString = dateFormatter.format(currentDate);

            reportItems.add(new ReportItem(dateString));
        } while(startDate.before(endDate));

        return reportItems;
    }

    public void calculateSalesForReportData(List<Order> orders, List<ReportItem> reportItems) {
        for (Order order : orders) {
            String orderDateString = dateFormatter.format(order.getOrderTime());

            ReportItem reportItem = new ReportItem(orderDateString);

            int index = reportItems.indexOf(reportItem);

            if (index >= 0) {
                reportItem = reportItems.get(index);

                reportItem.setGrossSales(order.getTotal());
                reportItem.setNetSales(order.getSubtotal() - order.getProductCost());
                reportItem.increaseOrdersCount();
            }
        }
    }
}

