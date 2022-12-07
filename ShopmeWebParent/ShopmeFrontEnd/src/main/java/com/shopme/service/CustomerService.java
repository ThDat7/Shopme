package com.shopme.service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceAlreadyExistException;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.CountryRepository;
import com.shopme.repository.CustomerRepository;
import com.shopme.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;



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

    public AuthenticationType getAuthenticationType(HttpServletRequest request) {
        String token = request.getHeader(JwtService.HEADER);
        return jwtService.getAuthenticationType(token);
    }

    public Customer getCustomer(HttpServletRequest request) {
        String token = request.getHeader(JwtService.HEADER);
        String username = jwtService.getUsername(token);

        return customerRepository.findByEmail(username)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void updateCustomerDetails(Customer customerInForm, HttpServletRequest request) {
        Customer customerInDb = getCustomer(request);

        if (customerInDb.getAuthenticationType()
                .equals(AuthenticationType.DATABASE)) {
            updateCustomerPassword(customerInForm, customerInDb);
        } else customerInForm.setPassword(
                customerInDb.getPassword());

        customerInForm.setId(customerInDb.getId());
        customerInForm.setEnabled(customerInDb.isEnabled());
        customerInForm.setCreatedTime(customerInDb.getCreatedTime());
        customerInForm.setVerificationCode(customerInDb.getVerificationCode());
        customerInForm.setAuthenticationType(customerInDb.getAuthenticationType());

        customerRepository.save(customerInForm);
    }

    private void updateCustomerPassword(Customer customerInForm, Customer customerInDb) {
        String newPassword = customerInForm.getPassword();
        if (newPassword == null || newPassword.isEmpty())
            customerInForm.setPassword(
                    customerInDb.getPassword());
        else {
            String passwordEncoded = passwordEncoder.encode(
                    customerInForm.getPassword());
            customerInForm.setPassword(passwordEncoded);
        }
    }
}
