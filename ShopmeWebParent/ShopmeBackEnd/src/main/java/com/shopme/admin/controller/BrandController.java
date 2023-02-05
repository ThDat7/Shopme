package com.shopme.admin.controller;

import com.shopme.admin.dto.BrandDTO;
import com.shopme.admin.service.BrandService;
import com.shopme.common.dto.CategoryDTO;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BrandService service;


    @GetMapping("")
    public ResponseEntity getAll(@RequestParam HashMap<String, String> requestParams) {
        return ResponseEntity.ok(
                mapToDTO(service.getAll(requestParams))
        );
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestPart("brand") BrandDTO brandDTO,
                       @RequestPart(value = "image", required = false)
                                    MultipartFile multipartFile) {
        Brand brand = mapToEntity(brandDTO);
        service.create(brand, multipartFile);
    }

    @PostMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void edit(@RequestPart("brand") BrandDTO brandDTO,
                     @PathVariable int id,
                     @RequestPart(name = "image", required = false)
                                    MultipartFile multipartFile) {
        Brand brand = mapToEntity(brandDTO);
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

    private CategoryDTO convertToCategoryDTO(Category category) {
        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        return categoryDTO;
    }

    private Brand mapToEntity(BrandDTO brandDTO) {
        return modelMapper.map(brandDTO, Brand.class);
    }

    private BrandDTO mapToDTO(Brand brand) {
        return modelMapper.map(brand, BrandDTO.class);
    }

    private Page<BrandDTO> mapToDTO(Page<Brand> brandPage) {
        return brandPage.map(new Function<Brand, BrandDTO>() {
            @Override
            public BrandDTO apply(Brand brand) {
                return mapToDTO(brand);
            }
        });
    }


}
