package com.shopme.admin.controller;

import com.shopme.admin.dto.product.ProductInfoDTO;
import com.shopme.admin.dto.product.ProductListDTO;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.service.ProductImageService;
import com.shopme.admin.service.ProductService;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Function;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam HashMap<String, String> requestParams)  {
        return ResponseEntity.ok().body(
                mapToDTO(productService.getAll(requestParams)));
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public void createProduct(@RequestPart("product") ProductInfoDTO productInfoDTO,
                              @RequestPart(name = "fileImage")
                              MultipartFile mainImageMultipart,
                              @RequestPart(name = "extraImage")
                              MultipartFile[] extraImageMultipart,
                              @RequestPart(name = "details", required = false)
                              HashSet<ProductDetail> details) {
        Product product = mapToEntity(productInfoDTO);

        productService.create(product);

        productImageService.saveImage(mainImageMultipart, extraImageMultipart, product);

        productService.saveDetails(product, details);
    }

    @PostMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public void edit(@RequestPart("product") ProductInfoDTO productInfoDTO,
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
        Product product = mapToEntity(productInfoDTO);
        if (loggedUser.hasRole("Salesperson")) {
            productService.saveProductPrice(id, product);
            return;
        }

        productService.edit(id, product);

        productImageService.setExistingExtraImageNames(imageIDs, imageNames, product);
        productImageService.setNewExtraImageNames(extraImageMultipart, product);
        productImageService.saveUploadedImages(mainImageMultipart, extraImageMultipart, product);
        productImageService.deleteExtraImagesWereRemovedOnForm(product);

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
        return ResponseEntity.ok().body(mapToDTO(productService.getById(id)));
    }

    @PostMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        productService.delete(id);
    }

    @GetMapping("/get_info/{id}")
    public ResponseEntity<?> getProductInfo(@RequestParam("id") int id) {
        return ResponseEntity.ok(mapToDTO(productService.getById(id)));
    }

    private Product mapToEntity(ProductInfoDTO productInfoDTO) {
        return modelMapper.map(productInfoDTO, Product.class);
    }

    private ProductInfoDTO mapToDTO(Product product) {
        return modelMapper.map(product, ProductInfoDTO.class);
    }

    private Page<ProductListDTO> mapToDTO(Page<Product> productPage) {
        return productPage.map(new Function<Product, ProductListDTO>() {
            @Override
            public ProductListDTO apply(Product product) {
                return modelMapper.map(product, ProductListDTO.class);
            }
        });
    }
}
