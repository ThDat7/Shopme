package com.shopme.admin.service;

import com.shopme.admin.repository.ProductRepository;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductDetail;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.common.metamodel.Brand_;
import com.shopme.common.metamodel.Category_;
import com.shopme.common.metamodel.Product_;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationHelper;
import com.shopme.common.specification.SpecificationOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

//    private static final int PRODUCT_PER_PAGE = 10;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductRepository productRepository;

    public Page<Product> getAll(HashMap<String, String> requestParams) {
        Specification specification = Specification.not(null);

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            ProductParamFilter enumKey;
            try {
                enumKey = ProductParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case keyword: {
                    String keywordSearch = value;
                    Filter searchName = Filter.builder()
                            .field(Product_.NAME).build();

                    Filter searchShortDescription = Filter.builder()
                            .field(Product_.SHORT_DESCRIPTION).build();

                    Filter searchFulLDescription = Filter.builder()
                            .field(Product_.FULL_DESCRIPTION).build();

                    Filter searchBrandName = Filter.builder()
                            .joinTables(Arrays.asList(Product_.BRAND))
                            .field(Brand_.NAME).build();

                    Filter searchCategoryName = Filter.builder()
                            .joinTables(Arrays.asList(Product_.CATEGORY))
                            .field(Product_.NAME).build();

                    List<Filter> searchFilters = Arrays.asList(
                            searchName, searchShortDescription, searchFulLDescription,
                            searchBrandName, searchCategoryName
                    );

                    searchFilters.forEach(filter -> {
                        filter.setValue(keywordSearch);
                        filter.setOperator(SpecificationOperator.LIKE);
                    });

                    specification = specification.and(SpecificationHelper
                            .filterSpecification(searchFilters));

                    break;
                }

                case categoryId: {
                    int categoryId = Integer.valueOf(value);
                    String categoryIdParentMatch = "-" + categoryId + "-";

                    Filter matchIdFilter = Filter.builder()
                            .joinTables(Arrays.asList(Product_.CATEGORY))
                            .field(Category_.ID)
                            .value(categoryId)
                            .operator(SpecificationOperator.EQUALS).build();

                    Filter matchIdParentFilter = Filter.builder()
                            .joinTables(Arrays.asList(Product_.CATEGORY))
                            .field(Category_.ALL_PARENT_IDS)
                            .value(categoryIdParentMatch)
                            .operator(SpecificationOperator.LIKE).build();

                    Specification matchIdParentSpec = SpecificationHelper
                            .createSpecification(matchIdParentFilter);

                    specification.and(
                            SpecificationHelper.createSpecification(matchIdFilter)
                                    .or(matchIdParentSpec)
                    );
                    break;
                }

                case brandId: {
                    int brandId = Integer.valueOf(value);

                    Filter matchIdFilter = Filter.builder()
                            .joinTables(Arrays.asList(Product_.BRAND))
                            .field(Brand_.ID)
                            .value(brandId)
                            .operator(SpecificationOperator.EQUALS).build();

                    specification.and(
                            SpecificationHelper.createSpecification(matchIdFilter)
                    );
                    break;
                }
            }
        }

        Pageable pageable = RequestParamsHelper.getPageableFromParamRequest(requestParams);

        return productRepository.findAll(specification, pageable);
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

        alias = alias.replace(" ", "-");
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
