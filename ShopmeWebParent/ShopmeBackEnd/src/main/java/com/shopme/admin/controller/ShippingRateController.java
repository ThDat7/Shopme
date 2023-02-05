package com.shopme.admin.controller;

import com.shopme.admin.service.ShippingRateService;
import com.shopme.common.entity.ShippingRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/shipping_rates")
public class ShippingRateController {
    @Autowired
    private ShippingRateService shippingRateService;

    @GetMapping
    public ResponseEntity<?> getAll(HashMap<String, String> requestParams) {
        return ResponseEntity.ok(shippingRateService.getAll(requestParams));
    }

    @PostMapping("edit/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void edit(ShippingRate shippingRate
            , @PathVariable("id") int id) {
        shippingRateService.edit(id, shippingRate);
    }

    @GetMapping("delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") int id) {
        shippingRateService.delete(id);
    }

    @PostMapping("/cod/{id}/enabled/{supported}")
    public void updateCODSupport(@PathVariable("id") int id,
                                   @PathVariable("supported") boolean isSupport) {
        shippingRateService.updateCODSupport(id, isSupport);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public void create(ShippingRate shippingRate) {
        shippingRateService.save(shippingRate);
    }

    @PostMapping("/get_shipping_cost")
    public ResponseEntity<?> calculateShippingCost(int productId,
                                                   Integer countryId,
                                                   String state) {
        float shippingCost = shippingRateService
                .calculateShippingCost(productId, countryId, state);

        return ResponseEntity.ok(shippingCost);
    }


}
