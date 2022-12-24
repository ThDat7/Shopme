package com.shopme.admin.service.report;

import com.shopme.admin.payload.response.ReportItem;
import com.shopme.admin.payload.response.ReportType;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.common.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MasterOrderReportService extends AbstractReportService {
    @Autowired
    private OrderRepository orderRepository;

    public List<ReportItem> getReportDataByPeriod(String period) {
        switch (period) {
            case "last_7_days": return getReportDataLast7Days(ReportType.DAY);
            case "last_28_days": return getReportDataLast28Days(ReportType.DAY);
            case "last_6_months": return getReportDataLast6Months(ReportType.MONTH);
            case "last_year": return getReportDataLastYear(ReportType.MONTH);

            default: return getReportDataLast7Days(ReportType.DAY);
        }
    }

    private List<ReportItem> createReportData(Date startTime, Date endTime,
                                              ReportType reportType) {
        List<ReportItem> reportItems = new ArrayList<>();

        Calendar startDate = Calendar.getInstance();
        startDate.setTime(startTime);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(endTime);

        Date currentDate = startDate.getTime();
        String dateString = dateFormatter.format(currentDate);

        reportItems.add(new ReportItem(dateString));

        do {
            if (reportType.equals(ReportType.DAY)) startDate.add(Calendar.DAY_OF_MONTH, 1);
            else if (reportType.equals(ReportType.MONTH)) startDate.add(Calendar.MONTH, 1);
            currentDate = startDate.getTime();
            dateString = dateFormatter.format(currentDate);

            reportItems.add(new ReportItem(dateString));
        } while(startDate.before(endDate));

        return reportItems;
    }

    private void calculateSalesForReportData(List<Order> orders, List<ReportItem> reportItems) {
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

    @Override
    protected List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType) {
        List<Order> listOrders = orderRepository.findByOrderTimeBetween(startTime, endTime);

        List<ReportItem> listReportItems = createReportData(startTime, endTime, reportType);

        calculateSalesForReportData(listOrders, listReportItems);

        return listReportItems;
    }
}

