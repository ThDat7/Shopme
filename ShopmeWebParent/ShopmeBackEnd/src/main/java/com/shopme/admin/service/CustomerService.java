package com.shopme.admin.service;

import com.shopme.admin.repository.CustomerRepository;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.common.metamodel.*;
import com.shopme.common.paramFilter.ProductParamFilter;
import com.shopme.common.paramFilter.RequestParamsHelper;
import com.shopme.common.specification.Filter;
import com.shopme.common.specification.SpecificationHelper;
import com.shopme.common.specification.SpecificationOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    public Page<Customer> getAll(HashMap<String, String> requestParams) {
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
                    Filter searchLastName = Filter.builder()
                            .field(Customer_.LAST_NAME).build();
                    Filter searchFirstName = Filter.builder()
                            .field(Customer_.FIRST_NAME).build();

                    Filter searchAddressLine1 = Filter.builder()
                            .field(Customer_.ADDRESS_LINE_1).build();

                    Filter searchAddressLine2 = Filter.builder()
                            .field(Customer_.ADDRESS_LINE_2).build();

                    Filter searchCity = Filter.builder()
                            .field(Customer_.CITY).build();

                    Filter searchState = Filter.builder()
                            .field(Customer_.STATE).build();

                    Filter searchPostalCode = Filter.builder()
                            .field(Customer_.POSTAL_CODE).build();

                    Filter searchCountry = Filter.builder()
                            .joinTables(Arrays.asList(Customer_.COUNTRY))
                            .field(Country_.NAME).build();

                    List<Filter> searchFilters = Arrays.asList(
                            searchLastName, searchFirstName, searchAddressLine1,
                            searchAddressLine2, searchCity, searchState,
                            searchPostalCode, searchCountry
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

        return customerRepository.findAll(specification, pageable);
    }

    public Customer get(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void save(Integer id, Customer customerInForm) {
        Customer customerInDB = customerRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        if (!customerInForm.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(customerInForm.getPassword());
            customerInForm.setPassword(encodedPassword);
        } else {
            customerInForm.setPassword(customerInDB.getPassword());
        }

        customerInForm.setId(id);
        customerInForm.setEnabled(customerInDB.isEnabled());
        customerInForm.setCreatedTime(customerInDB.getCreatedTime());
        customerInForm.setVerificationCode(customerInDB.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDB.getAuthenticationType());
        customerInForm.setResetPasswordToken(customerInDB.getResetPasswordToken());

        customerRepository.save(customerInForm);
    }


    private void encodePassword(Customer customer) {
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);
    }

    public void delete(Integer id) {
        if (!customerRepository.existsById(id))
            throw new ResourceNotFoundException();

        customerRepository.deleteById(id);
    }

    public void updateStatus(Integer id, boolean status) {
        if (!customerRepository.existsById(id))
            throw new ResourceNotFoundException();
        customerRepository.updateStatus(id, status);
    }

    public void valueEmailUnique(String email) {
        if (customerRepository.existsByEmail(email))
            throw new ResourceAlreadyExistException();
    }
}
