package com.shopme.admin.service;

import com.shopme.admin.repository.ProductRepository;
import com.shopme.admin.repository.ReviewRepository;
import com.shopme.common.entity.Review;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.common.metamodel.*;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.common.paramFilter.ReviewParamFilter;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationHelper;
import com.shopme.common.specification.SpecificationOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    public Page<Review> getAll(HashMap<String, String> requestParams) {
        Specification specification = Specification.not(null);

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            ReviewParamFilter enumKey;
            try {
                enumKey = ReviewParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case keyword: {
                    String keywordSearch = value;
                    Filter searchHeadLine = Filter.builder()
                            .field(Review_.HEAD_LINE).build();

                    Filter searchProductName = Filter.builder()
                            .joinTables(Arrays.asList(Review_.PRODUCT))
                            .field(Product_.NAME).build();

                    Filter searchCustomerFirstName = Filter.builder()
                            .joinTables(Arrays.asList(Review_.CUSTOMER))
                            .field(Customer_.FIRST_NAME).build();

                    Filter searchCustomerLastName = Filter.builder()
                            .joinTables(Arrays.asList(Review_.CUSTOMER))
                            .field(Customer_.LAST_NAME).build();

                    List<Filter> searchFilters = Arrays.asList(
                            searchHeadLine, searchProductName, searchCustomerFirstName,
                            searchCustomerLastName
                    );

                    searchFilters.forEach(filter -> {
                        filter.setValue(keywordSearch);
                        filter.setOperator(SpecificationOperator.LIKE);
                    });

                    specification = specification.and(SpecificationHelper
                            .filterSpecification(searchFilters));

                    break;
                }
            }
        }

        Pageable pageable = RequestParamsHelper.getPageableFromParamRequest(requestParams);

        return productRepository.findAll(specification, pageable);
    }

    public Review get(int id) {
        return reviewRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void save(Review review) {
        review.setId(null);
        reviewRepository.save(review);
        productRepository.updateAvgRatingAndReviewCount(
                review.getOrderDetail().getProduct().getId());
    }

    public void edit(int id, Review review) {
        review.setId(id);
        reviewRepository.save(review);
    }

    public void delete(int id) {
        reviewRepository.deleteById(id);
    }
}
