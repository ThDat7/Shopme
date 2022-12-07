package com.shopme.service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.CountryRepository;
import com.shopme.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CustomerService {
    private CustomerRepository customerRepository;

    private CountryRepository countryRepository;


    public CustomerService(CustomerRepository customerRepository, CountryRepository countryRepository) {
        this.customerRepository = customerRepository;
        this.countryRepository = countryRepository;
    }

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

    public void updateAuthenticationType(Customer customer,
                                         AuthenticationType authenticationType) {
        if (customer.getAuthenticationType().equals(authenticationType)) return;

        customerRepository.updateAuthenticationType(customer.getId(), authenticationType);
    }

    public Optional<Customer> getByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer addNewCustomerUponOAuthLogin(String name, String email, String countryCode,
                                                  AuthenticationType authenticationType) {
        Customer customer = new Customer();
        setName(customer, name);
        customer.setEmail(email);

        Optional<Country> opCountry = countryRepository.findByCode(countryCode);
        Country country = opCountry.isPresent() ? opCountry.get() : null;
        customer.setCountry(country);

        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setAuthenticationType(AuthenticationType.GOOGLE);
        customer.setPassword("");
        customer.setAuthenticationType(authenticationType);
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setState("");
        customer.setPhoneNumber("");
        customer.setPostalCode("");

        return customerRepository.save(customer);
    }

    private void setName(Customer customer, String name) {
        String[] namePart = name.split(" ");

        if (namePart.length < 2) {
            customer.setFirstName(name);
            customer.setLastName("");
        } else {
            String firstName = namePart[0];
            customer.setFirstName(firstName);

            String lastName = namePart[1];
            customer.setLastName(lastName);
        }
    }
}
