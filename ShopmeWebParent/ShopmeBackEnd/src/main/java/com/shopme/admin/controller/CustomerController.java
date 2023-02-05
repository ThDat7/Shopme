package com.shopme.admin.controller;

import com.shopme.admin.service.CustomerService;
import com.shopme.common.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getAll(HashMap<String, String> requestParams) {
        return ResponseEntity.ok(customerService.getAll(requestParams));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable("id") int id) {
        return ResponseEntity.ok(customerService.get(id));
    }

    @GetMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        customerService.delete(id);
    }

    @PostMapping("edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void edit(Customer customer, @PathVariable("id") int id) {
        customerService.save(id, customer);
    }

    @GetMapping("/{id}/update_status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public void updateStatus(@PathVariable("id") int id,
                             @PathVariable("status") boolean status) {
        customerService.updateStatus(id, status);
    }

    @PostMapping("/check_email")
    @ResponseStatus(HttpStatus.OK)
    public void checkEmailUnique(@RequestParam("email") String email) {
        customerService.valueEmailUnique(email);
    }
}
