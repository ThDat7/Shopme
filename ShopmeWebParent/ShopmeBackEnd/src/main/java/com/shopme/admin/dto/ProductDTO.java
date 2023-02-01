package com.shopme.admin.dto;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ProductDTO {
    private String name;
    private String imagePath;
    private float price;
    private float cost;
}
