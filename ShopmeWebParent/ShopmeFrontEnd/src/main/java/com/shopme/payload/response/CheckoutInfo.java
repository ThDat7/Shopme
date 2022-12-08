package com.shopme.payload.response;

import lombok.*;

@Getter
@Setter @AllArgsConstructor @NoArgsConstructor
@Builder
public class CheckoutInfo {
    private float productCost;
    private float productTotal;
    private float shippingCostTotal;
    private float paymentTotal;
    private int deliverDays;
    private boolean codSupported;
}
