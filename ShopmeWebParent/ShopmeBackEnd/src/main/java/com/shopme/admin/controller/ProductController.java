package com.shopme.admin.controller;

import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.ProductImageService;
import com.shopme.admin.service.ProductService;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    private final ProductImageService productImageService;

    public ProductController(ProductService productService, ProductImageService productImageService) {
        this.productService = productService;
        this.productImageService = productImageService;
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
                              HashSet<ProductDetail> details) {
        productService.create(product);

        productImageService.saveImage(mainImageMultipart, extraImageMultipart, product);

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
                     @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                     @RequestParam(name = "imageNames", required = false) String[] imageNames,
                     @RequestParam(name = "details", required = false)
                         HashSet<ProductDetail> details,
                     @AuthenticationPrincipal ShopmeUserDetails loggedUser) {
        if (loggedUser.hasRole("Salesperson")) {
            productService.saveProductPrice(id, product);
            return;
        }

        productService.edit(id, product);

        productImageService.setExistingExtraImageNames(imageIDs, imageNames, product);
        productImageService.setNewExtraImageNames(extraImageMultipart, product);
        productImageService.deleteExtraImagesWereRemovedOnForm(product);
        productImageService.saveUploadedImages(mainImageMultipart, extraImageMultipart, product);

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
