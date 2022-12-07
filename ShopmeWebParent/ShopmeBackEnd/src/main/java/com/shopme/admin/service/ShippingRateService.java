package com.shopme.admin.service;

import com.shopme.admin.repository.ShippingRateRepository;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingRateService {
    @Autowired
    private ShippingRateRepository shippingRateRepository;

    public List<ShippingRate> getAll() {
        return shippingRateRepository.findAll();
    }

    public void edit(int id, ShippingRate shippingRate) {
        if (!shippingRateRepository
                .existsById(id))
            throw new ResourceNotFoundException();

        shippingRate.setId(id);
        shippingRateRepository.save(shippingRate);
    }

    public void updateCODSupport(int id, boolean isSupport) {
        shippingRateRepository.updateCODSupport(id, isSupport);
    }

    public ShippingRate get(int id) {
        return shippingRateRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void delete(int id) {
        shippingRateRepository.deleteById(id);
    }

    public void save(ShippingRate shippingRate) {
        shippingRateRepository.save(shippingRate);
    }
}
