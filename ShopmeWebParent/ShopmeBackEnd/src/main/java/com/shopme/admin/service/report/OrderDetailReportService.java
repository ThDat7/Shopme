package com.shopme.admin.service.report;

import com.shopme.admin.payload.response.ReportItem;
import com.shopme.admin.payload.response.ReportType;
import com.shopme.admin.repository.OrderDetailRepository;
import com.shopme.common.entity.OrderDetail;
import com.shopme.common.exception.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderDetailReportService extends AbstractReportService{
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<ReportItem> getReportDataByPeriod(ReportType reportType, String period) {
        switch (period) {
            case "last_7_days":
                return getReportDataLast7Days(reportType);
            case "last_28_days":
                return getReportDataLast28Days(reportType);
            case "last_6_months":
                return getReportDataLast6Months(reportType);
            case "last_year":
                return getReportDataLastYear(reportType);

            default:
                return getReportDataLast7Days(reportType);
        }
    }

    @Override
    protected List<ReportItem> getReportDataByDateRangeInternal(Date startTime, Date endTime, ReportType reportType) {
        if (!reportType.equals(ReportType.CATEGORY)) throw new InvalidArgumentException();

        List<OrderDetail> listOrderDetails = null;

        if (reportType.equals(ReportType.CATEGORY))
            listOrderDetails = orderDetailRepository
                    .findWithCategoryAndTimeBetween(startTime, endTime);
        else if (reportType.equals(ReportType.PRODUCT))
            listOrderDetails = orderDetailRepository
                    .findWithProductAndTimeBetween(startTime, endTime);


        List<ReportItem> listReportItems = new ArrayList<>();

        for (OrderDetail orderDetail : listOrderDetails) {
            String identifier = "";

            if (reportType.equals(ReportType.CATEGORY))
                identifier = orderDetail.getProduct().getCategory().getName();
            else if (reportType.equals(ReportType.PRODUCT))
                identifier = orderDetail.getProduct().getName();

            ReportItem reportItem = new ReportItem(identifier);

            float grossSales = orderDetail.getSubtotal() + orderDetail.getShippingCost();
            float netSales = orderDetail.getSubtotal() - orderDetail.getProductCost();

            int itemIndex = listReportItems.indexOf(reportItem);

            if (itemIndex >= 0) {
                reportItem = listReportItems.get(itemIndex);
                reportItem.addGrossSale(grossSales);
                reportItem.addNetSales(netSales);
                reportItem.increaseProductsCount(orderDetail.getQuantity());
            } else {
                reportItem.setGrossSales(grossSales);
                reportItem.setNetSales(netSales);
                reportItem.setOrdersCount(orderDetail.getQuantity());
                listReportItems.add(reportItem);
            }
        }

        return listReportItems;
    }
}
