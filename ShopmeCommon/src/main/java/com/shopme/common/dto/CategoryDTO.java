package com.shopme.common.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategoryDTO {
    private int id;

    private String alias;

    private String name;

    private String image;

    private boolean enabled;

    @JsonBackReference
    private Set<CategoryDTO> children;
}
