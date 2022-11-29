package com.shopme.admin.service;

import com.shopme.admin.exception.ResourceAlreadyExistException;
import com.shopme.admin.exception.ResourceNotFoundException;
import com.shopme.admin.repository.ProductRepository;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public void validateNameUnique(String name) {
        if (productRepository.existsByName(name))
            throw new ResourceAlreadyExistException();
    }

    public void create(Product product) {
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());
        setAlias(product);

        productRepository.save(product);

    }

    public void saveImages(Product product, MultipartFile mainImageMultipart,
                          MultipartFile[] extraImageMultipart) {
        productImageService.saveImage(mainImageMultipart, extraImageMultipart, product);
    }

    public void saveDetails(Product product, HashMap<String, String> details) {
        productDetailService.setProductDetails(details, product);
    }

    public void edit(int id, Product product) {
        product.setId(id);
        product.setUpdatedTime(new Date());
        setAlias(product);

        productRepository.save(product);
    }

    private void setAlias(Product product) {
        String alias = "";
        if (product.getAlias().isEmpty() || product.getAlias() == null)
            alias = product.getName();

        alias.replace(" ", "-");
        product.setAlias(alias);
    }
    @Transactional
    public void updateStatus(int id, boolean status) {
        if (!productRepository.existsById(id))
            throw new ResourceNotFoundException();

        productRepository.updateStatus(id, status);
    }

    @Transactional
    public void delete(int id) {
        if (!productRepository.existsById(id))
            throw new ResourceAlreadyExistException();

        productRepository.deleteById(id);
        productImageService.removeImageDir(id);
    }


    public Product getById(int id) {
        return productRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }
}
