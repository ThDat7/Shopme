package com.shopme.admin.controller;

import com.shopme.admin.service.BrandService;
import com.shopme.common.entity.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private BrandService service;

    public BrandController(BrandService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity getAll(@RequestParam HashMap<String, String> requestParams) {
        List<Brand> brands = service.getAll(requestParams).getContent();
        return ResponseEntity.ok(brands);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(Brand brand, @RequestParam(value = "image", required = false)
                                    MultipartFile multipartFile) {
        service.create(brand, multipartFile);
    }

    @PostMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void edit(Brand brand, @PathVariable int id,@RequestParam(name = "image", required = false)
                                    MultipartFile multipartFile) {
        service.edit(id, brand, multipartFile);
    }

    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }

    @PostMapping("/check-name")
    @ResponseStatus(HttpStatus.OK)
    public void checkName(@RequestParam String name) {
        service.validateNameUnique(name);
    }
}
