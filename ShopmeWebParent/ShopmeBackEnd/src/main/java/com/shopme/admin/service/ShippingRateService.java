package com.shopme.admin.service;

import com.shopme.admin.repository.ProductRepository;
import com.shopme.admin.repository.ShippingRateRepository;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.common.metamodel.*;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.common.paramFilter.ShippingRateParamFilter;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationHelper;
import com.shopme.common.specification.SpecificationOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class ShippingRateService {
    @Autowired
    private ShippingRateRepository shippingRateRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("${shopme.app.shipping.dim-divisor}")
    private static int DIM_DIVISOR;

    public Page<ShippingRate> getAll(HashMap<String, String> requestParams) {
        Specification specification = Specification.not(null);

        for (String key : requestParams.keySet()) {
            String value = requestParams.get(key);

            ShippingRateParamFilter enumKey;
            try {
                enumKey = ShippingRateParamFilter.valueOf(key);
            } catch(IllegalArgumentException e) { continue; }
            switch (enumKey) {
                case keyword: {
                    String keywordSearch = value;
                    Filter searchCountryName = Filter.builder()
                            .joinTables(Arrays.asList(ShippingRate_.COUNTRY))
                            .field(Country_.NAME).build();

                    Filter searchState = Filter.builder()
                            .field(ShippingRate_.STATE).build();


                    List<Filter> searchFilters = Arrays.asList(
                            searchCountryName, searchState
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

    public void edit(int id, ShippingRate shippingRate) {
        if (!shippingRateRepository
                .existsById(id))
            throw new ResourceNotFoundException();

        shippingRate.setId(id);
        shippingRateRepository.save(shippingRate);
    }

    public void updateCODSupport(int id, boolean isSupport) {
        shippingRateRepository.updateCODSupport(id, isSupport);
    }

    public ShippingRate get(int id) {
        return shippingRateRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void delete(int id) {
        shippingRateRepository.deleteById(id);
    }

    public void save(ShippingRate shippingRate) {
        shippingRateRepository.save(shippingRate);
    }

    public float calculateShippingCost(Integer productId, Integer countryId, String state) {
        ShippingRate shippingRate = shippingRateRepository
                .findByCountryAndState(countryId, state)
                .orElseThrow(ResourceNotFoundException::new);

        Product product = productRepository.findById(productId)
                .orElseThrow(ResourceNotFoundException::new);

        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight())
                / DIM_DIVISOR;
        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

        return finalWeight * shippingRate.getRate();
    }
}
