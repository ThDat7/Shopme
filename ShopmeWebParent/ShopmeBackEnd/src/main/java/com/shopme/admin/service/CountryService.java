package com.shopme.admin.service;

import com.shopme.admin.repository.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public List<Country> findAll() {
        return countryRepository.findAll();
    }

    public int save(Country country) {
        return countryRepository.save(country).getId();
    }

    public void delete(int id) {
        if (!countryRepository.existsById(id))
            throw new ResourceNotFoundException();

        countryRepository.deleteById(id);
    }
}
