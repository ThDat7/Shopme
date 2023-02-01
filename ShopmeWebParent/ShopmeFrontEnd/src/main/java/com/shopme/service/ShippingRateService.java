package com.shopme.service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.AddressRepository;
import com.shopme.repository.CustomerRepository;
import com.shopme.repository.ShippingRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingRateService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ShippingRateRepository shippingRateRepository;

    @Autowired
    private AddressRepository addressRepository;

    private ShippingRate getForCustomerInfo(Customer customer) {

        String state = customer.getState();

        return shippingRateRepository
                .findByCountryAndState(customer.getCountry(), state)
                .get();
    }

    private ShippingRate getForAddress(Address address) {
        String state = address.getState();
        if (state == null || state.isEmpty())
            state = address.getCity();

        return shippingRateRepository
                .findByCountryAndState(address.getCountry(), state)
                .get();
    }

    public ShippingRate getForCustomer(int customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(ResourceNotFoundException::new);

        Address defaultAddress = addressRepository.findDefault(customer)
                .get();

        ShippingRate shippingRate = null;

        if (defaultAddress != null)
            shippingRate = getForAddress(defaultAddress);
        else shippingRate = getForCustomerInfo(customer);

        if (shippingRate == null)
            throw new ResourceNotFoundException("Can't find any Shipping rate for your" +
                    "country. Sorry we don't support for your country.");
        return shippingRate;
    }
}
