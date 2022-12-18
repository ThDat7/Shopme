package com.shopme.payload.request;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class OrderReturnRequest {
    private Integer orderId;
    private String reason;
    private String note;
}
