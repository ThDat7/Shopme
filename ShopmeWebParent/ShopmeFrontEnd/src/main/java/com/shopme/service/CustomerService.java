package com.shopme.service;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;


    public void save(Customer customer) {
    }

    public void valueEmailUnique(String email) {
        if (customerRepository.existsByEmail(email))
            throw new ResourceAlreadyExistException();
    }

    public void verify(String verificationCode) {
        Customer customer = customerRepository.findByVerificationCode(verificationCode)
                        .orElseThrow(ResourceNotFoundException::new);

        customerRepository.enable(customer.getId());
    }
}
