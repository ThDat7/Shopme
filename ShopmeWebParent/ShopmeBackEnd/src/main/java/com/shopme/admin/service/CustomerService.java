package com.shopme.admin.service;

import com.shopme.admin.repository.CustomerRepository;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAll() {
        return customerRepository.findAll();
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
