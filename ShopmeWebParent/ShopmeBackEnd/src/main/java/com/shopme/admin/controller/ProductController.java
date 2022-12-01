package com.shopme.admin.controller;

import com.shopme.admin.service.ProductService;
import com.shopme.common.entity.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam HashMap<String, String> requestParams)  {
        return ResponseEntity.ok().body(
                productService.getAll(requestParams));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createProduct(Product product,
                              @RequestParam(name = "fileImage")
                              MultipartFile mainImageMultipart,
                              @RequestParam(name = "extraImage")
                              MultipartFile[] extraImageMultipart,
                              @RequestParam(name = "details", required = false)
                              HashMap<String, String> details) {
        productService.create(product);
        productService.saveImages(product, mainImageMultipart, extraImageMultipart);
        productService.saveDetails(product, details);
    }

    @PostMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public void edit(Product product,
                              @PathVariable(name = "id") int id,
                              @RequestParam(name = "fileImage")
                              MultipartFile mainImageMultipart,
                              @RequestParam(name = "extraImage")
                              MultipartFile[] extraImageMultipart,
                              @RequestParam(name = "details", required = false)
                              HashMap<String, String> details) {
        productService.edit(id, product);
        productService.saveImages(product, mainImageMultipart, extraImageMultipart);
        productService.saveDetails(product, details);
    }

    @PostMapping("/check-name")
    @ResponseStatus
    public void checkName(@RequestParam(name = "name") String name) {
        productService.validateNameUnique(name);
    }

    @PostMapping("/{id}/update-status/{status}")
    public void updateStatus(@PathVariable("id") int id,
                             @PathVariable("status") boolean status) {
        productService.updateStatus(id, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(productService.getById(id));
    }

    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        productService.delete(id);
    }
}
