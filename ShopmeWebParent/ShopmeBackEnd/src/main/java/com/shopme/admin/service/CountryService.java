package com.shopme.admin.service;

import com.shopme.admin.repository.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.common.metamodel.Country_;
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

import java.util.Arrays;
import java.util.HashMap;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public Page<Country> getAll(HashMap<String, String> requestParams) {
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
                            .field(Country_.NAME).build();

                    searchName.setValue(keywordSearch);
                    searchName.setOperator(SpecificationOperator.LIKE);

                    specification = specification.and(SpecificationHelper
                            .filterSpecification(Arrays.asList(searchName)));

                    break;
                }
            }
        }

        Pageable pageable = RequestParamsHelper.getPageableFromParamRequest(requestParams);

        return countryRepository.findAll(specification, pageable);
    }

    public int save(Country country) {
        return countryRepository.save(country).getId();
    }

    public void delete(int id) {
        if (!countryRepository.existsById(id))
            throw new ResourceNotFoundException();

        countryRepository.deleteById(id);
    }
}
