package com.shopme.admin.controller;

import com.shopme.admin.service.CategoryService;
import com.shopme.common.entity.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService service) {
        this.categoryService = service;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(
            @RequestParam HashMap<String, String> requestParams
            ) {
        return ResponseEntity.ok(categoryService.getAll(requestParams));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(Category category) {
        categoryService.create(category);
    }

    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        categoryService.delete(id);
    }

    @PostMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void edit(Category category, @PathVariable("id") int id) {
        categoryService.edit(id, category);
    }

    @PostMapping("/check-name")
    @ResponseStatus(HttpStatus.OK)
    public void checkName(@RequestParam("name") String name) {
        categoryService.validateNameUnique(name);
    }

    @PostMapping("/check-alias")
    @ResponseStatus(HttpStatus.OK)
    public void checkAlias(@RequestParam("alias") String alias) {
        categoryService.validateAliasUnique(alias);
    }

    @GetMapping("/get-cate-in-form")
    public ResponseEntity getCategoriesUsedInForm() {
        List<Category> cateUsedInForm = categoryService.listCategoryUsedInForm();
        return ResponseEntity.ok(cateUsedInForm);
    }
}
