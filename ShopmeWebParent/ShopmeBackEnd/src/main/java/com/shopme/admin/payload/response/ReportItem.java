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

    private int productsCount;

    public void addGrossSale(float amount) {
        grossSales += amount;
    }

    public void addNetSales(float amount) {
        netSales += amount;
    }

    public void increaseOrdersCount() {
        ordersCount++;
    }

    public void increaseProductsCount(int count) {
        productsCount += count;
    }

    public ReportItem(String identifier) {
        this.identifier = identifier;
    }
}
