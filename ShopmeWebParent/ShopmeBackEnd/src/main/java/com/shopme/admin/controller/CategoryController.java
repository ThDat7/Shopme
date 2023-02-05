package com.shopme.admin.controller;

import com.shopme.admin.dto.CategoryCreateDTO;
import com.shopme.admin.dto.CategoryListDTO;
import com.shopme.admin.service.BrandService;
import com.shopme.admin.service.CategoryService;
import com.shopme.common.dto.CategoryDTO;
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
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("")
    public ResponseEntity<?> getAll(
            @RequestParam HashMap<String, String> requestParams
            ) {
        return ResponseEntity.ok(mapToDTO(categoryService.getAll(requestParams)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok(mapToDTO(categoryService.getById(id)));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestPart("category") CategoryCreateDTO categoryDTO,
                       @RequestPart(value = "image", required = false)
                       MultipartFile multipartFile) {
        Category category = mapToEntity(categoryDTO);
        categoryService.create(category);
    }

    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        categoryService.delete(id);
    }

    @PostMapping("/edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void edit(@RequestPart("category") CategoryCreateDTO categoryDTO,
                     @PathVariable("id") int id,
                     @RequestPart(value = "image", required = false)
                     MultipartFile multipartFile ){
        Category category = mapToEntity(categoryDTO);
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
        return ResponseEntity.ok(
                mapToDTO(categoryService.listCategoryUsedInForm())
        );
    }

    @GetMapping("/{id}/categories")
    public ResponseEntity<?> getCategoriesByBrand(@PathVariable(name = "id") int id) {
        return ResponseEntity.ok().body(
                mapToDTO(brandService.getCategoriesByBrandId(id))
        );
    }

    private Category mapToEntity(CategoryCreateDTO categoryCreateDTO) {
        return modelMapper.map(categoryCreateDTO, Category.class);
    }

    private CategoryListDTO mapToDTO(Category category) {
        return modelMapper.map(category, CategoryListDTO.class);
    }

    private Page<CategoryListDTO> mapToDTO(Page<Category> categoryPage) {
        return categoryPage.map(new Function<Category, CategoryListDTO>() {
            @Override
            public CategoryListDTO apply(Category category) {
                return mapToDTO(category);
            }
        });
    }

    private List<CategoryListDTO> mapToDTO(List<Category> categoryPage) {
        return categoryPage.stream().map(i -> mapToDTO(i))
                .collect(Collectors.toList());
    }
}
