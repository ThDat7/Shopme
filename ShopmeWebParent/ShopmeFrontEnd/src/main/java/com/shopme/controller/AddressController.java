package com.shopme.controller;

import com.shopme.common.entity.Address;
import com.shopme.security.JwtService;
import com.shopme.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/address_book")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtService jwtService;

    @GetMapping()
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        int customerId = getCustomerId(request);

        return ResponseEntity.ok(addressService
                .getByCustomer(customerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") int id,
                                 HttpServletRequest request) {
        int customerId = getCustomerId(request);

        return ResponseEntity.ok(
                addressService.get(id, customerId)
        );
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public void saveAddress(Address address,
                            HttpServletRequest request) {
        int customerId = getCustomerId(request);
        addressService.save(address, customerId);
    }

    @PostMapping("/{id}/edit")
    @ResponseStatus(HttpStatus.OK)
    public void edit(Address address,
                     @PathVariable("id") int id,
                     HttpServletRequest request) {
        int customerId = getCustomerId(request);
        addressService.edit(id, address, customerId);
    }

    @GetMapping("{id}/delete")
    public void delete(@PathVariable("id") int id,
                       HttpServletRequest request) {
        int customerId = getCustomerId(request);

        addressService.delete(id, customerId);
    }

    @PostMapping("/{id}/default")
    @ResponseStatus(HttpStatus.OK)
    public void setDefault(@PathVariable("id") int id,
                           HttpServletRequest request) {
        int customerId = getCustomerId(request);
        addressService.setDefault(id, customerId);
    }


    private int getCustomerId(HttpServletRequest request) {
        String token = request.getHeader(JwtService.HEADER);
        return jwtService.getCustomerId(token);
    }
}
