package com.shopme.admin.service;

import com.shopme.admin.repository.CustomerRepository;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    public Customer get(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void create(Customer customer) {
        customerRepository.save(customer);
    }

    public void edit(int id, Customer customer) {
        customerRepository.save(customer);
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
