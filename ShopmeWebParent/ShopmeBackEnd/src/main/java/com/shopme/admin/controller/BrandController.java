package com.shopme.admin.controller;

import com.shopme.admin.service.BrandService;
import com.shopme.common.dto.CategoryDTO;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brands")
public class BrandController {

    private BrandService service;

    private ModelMapper modelMapper;

    public BrandController(BrandService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
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

    @GetMapping("/{id}/categories")
    public ResponseEntity<?> getCategoriesByBrand(@PathVariable(name = "id") int id) {
        return ResponseEntity.ok().body(
                service.getCategoriesByBrandId(id)
                        .stream()
                        .map(this::convertToCategoryDTO)
                        .collect(Collectors.toList())
        );
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }


}
