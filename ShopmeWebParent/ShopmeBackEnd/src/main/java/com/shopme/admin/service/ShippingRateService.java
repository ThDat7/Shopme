package com.shopme.admin.service;

import com.shopme.admin.repository.ProductRepository;
import com.shopme.admin.repository.ShippingRateRepository;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShippingRateService {
    @Autowired
    private ShippingRateRepository shippingRateRepository;

    @Autowired
    private ProductRepository productRepository;

    @Value("${shopme.app.shipping.dim-divisor}")
    private static int DIM_DIVISOR;

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

    public float calculateShippingCost(Integer productId, Integer countryId, String state) {
        ShippingRate shippingRate = shippingRateRepository
                .findByCountryAndState(countryId, state)
                .orElseThrow(ResourceNotFoundException::new);

        Product product = productRepository.findById(productId)
                .orElseThrow(ResourceNotFoundException::new);

        float dimWeight = (product.getLength() * product.getWidth() * product.getHeight())
                / DIM_DIVISOR;
        float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;

        return finalWeight * shippingRate.getRate();
    }
}
