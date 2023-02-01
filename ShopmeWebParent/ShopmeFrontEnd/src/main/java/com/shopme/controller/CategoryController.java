package com.shopme.controller;

import com.shopme.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get-list-parent/{category_alias}")
    public ResponseEntity<?> getListParent(@PathVariable("category_alias")
                                               String category_alias) {
        return ResponseEntity.ok().body(
                categoryService.getCategoryParents(category_alias));
    }
}
