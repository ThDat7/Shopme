package com.shopme.admin.dto.product;

import lombok.*;


@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class ProductListDTO {
    private int id;
    private String name;
    private String mainImage;
    private boolean enabled;
    private String brandName;
    private String categoryName;
}
