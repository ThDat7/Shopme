package com.shopme.admin.controller;

import com.shopme.admin.service.CountryService;
import com.shopme.common.entity.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/countries")
public class CountryController {
    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam HashMap<String, String> requestParams) {
        return ResponseEntity.ok(countryService.getAll(requestParams));
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(Country country) {
        return ResponseEntity.ok(countryService.save(country));
    }

    @GetMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        countryService.delete(id);
    }

}
