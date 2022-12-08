package com.shopme.service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.exception.OutOfMaxAddressBook;
import com.shopme.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {

    @Value("${shopme.app.address_book.max-address_book-for-customer}")
    private Integer maxAddressBook;

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getByCustomer(int customerId) {
        return addressRepository.findByCustomer(new Customer(customerId));
    }

    public Address get(int id, int customerId) {
        return addressRepository.findByIdAndCustomer(id, new Customer(customerId))
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void save(Address address, int customerId) {
        Customer customer = new Customer(customerId);
        int countAddressNow = addressRepository.countByCustomer(customer);

        if (countAddressNow >= maxAddressBook)
            throw new OutOfMaxAddressBook();

        address.setCustomer(customer);
        addressRepository.save(address);
    }

    public void edit(int id, Address address, int customerId) {
        address.setId(id);
        address.setCustomer(new Customer(customerId));
        addressRepository.save(address);
    }

    public void delete(int id, int customerId) {
        addressRepository.deleteByIdAndCustomer(id, new Customer(customerId));
    }

    public void setDefault(int id, int customerId) {
        Customer customer = new Customer(customerId);

        if (!addressRepository
                .existsByIdAndCustomer(id, customer))
            throw new ResourceNotFoundException();

        addressRepository.setDefault(id, customer);
        addressRepository.setNoneDefaultForOther(id, customer);
    }

    public Address getDefault(int customerId) {
        return addressRepository.findDefault(new Customer(customerId))
                .orElseThrow(ResourceNotFoundException::new);
    }
}
