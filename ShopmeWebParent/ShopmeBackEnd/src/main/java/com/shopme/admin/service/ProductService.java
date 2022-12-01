package com.shopme.admin.service;

import com.shopme.admin.exception.ResourceAlreadyExistException;
import com.shopme.admin.exception.ResourceNotFoundException;
import com.shopme.admin.repository.ProductRepository;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import com.shopme.common.metamodel.Product_;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.specification.Filter;
import com.shopme.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class ProductService {

    private static final int PRODUCT_PER_PAGE = 10;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAll(HashMap<String, String> requestParams) {
        Sort sort = Sort.by(Product_.NAME).ascending();
        int pageIndex = 0;

        Set<Filter> filters = new HashSet<>();

        String keywordSearch = "";
        Integer categoryId = null;

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            ProductParamFilter enumKey;
            try {
                enumKey = ProductParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case keyword: {
                    keywordSearch = value;
                    break;
                }

                case order: {
                    if (value.equals("desc")) sort = sort.descending();
                    else sort = sort.ascending();
                    break;
                }

                case sortBy: {
                    sort = sort.by(value);
                    break;
                }

                case page: {
                    if (StringUtils.isInteger(value))
                        pageIndex = Integer.valueOf(value) - 1;
                    break;
                }

                case categoryId: {
                    categoryId = Integer.valueOf(value);
                    break;
                }
            }
        }

        Pageable pageable =  PageRequest.of(pageIndex, PRODUCT_PER_PAGE, sort);

        if (!keywordSearch.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryIdMatch = "-" + categoryId + "-";
                productRepository.searchInCategory(categoryId, categoryIdMatch,
                        keywordSearch, pageable);
            }
            return productRepository.findAll(keywordSearch, pageable);
        }

        if (categoryId != null && categoryId > 0) {
            String categoryIdMatch = "-" + categoryId + "-";
            return productRepository.findAllInCategory(categoryId, categoryIdMatch
                    , pageable);
        }

        return productRepository.findAll(pageable);
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

    public void edit(int id, Product product) {
        product.setId(id);
        product.setUpdatedTime(new Date());
        setAlias(product);

        productRepository.save(product);
    }

    public void saveProductPrice(int id, Product productInForm) {
        Product productInDB = productRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        productInDB.setCost(productInForm.getCost());
        productInDB.setPrice(productInForm.getPrice());
        productInDB.setDiscountPercent(productInForm.getDiscountPercent());

        productRepository.save(productInDB);
    }

    public void saveDetails(Product product, HashSet<ProductDetail> details) {
        productDetailService.setProductDetails(details, product);
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
