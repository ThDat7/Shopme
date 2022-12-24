package com.shopme.admin.payload.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ReportItem {
    private String identifier;
    private float grossSales;
    private float netSales;
    private int ordersCount;

    public void addGrossSale(float amount) {
        grossSales += amount;
    }

    public void addNetSales(float amount) {
        netSales += amount;
    }

    public void increaseOrdersCount() {
        ordersCount++;
    }

    public ReportItem(String identifier) {
        this.identifier = identifier;
    }
}
