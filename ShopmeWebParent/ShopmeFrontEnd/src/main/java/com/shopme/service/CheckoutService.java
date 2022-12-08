package com.shopme.service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ShippingRate;
import com.shopme.payload.response.CheckoutInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {
    private static final int DIM_DIVISOR = 139;

    public CheckoutInfo prepareCheckout(List<CartItem> cartItems
            , ShippingRate shippingRate) {
        float productCost = calculatorProductCost(cartItems);
        float productTotal = calculatorProductTotal(cartItems);
        float shippingCostTotal = calculatorShippingCost(cartItems, shippingRate);
        float paymentTotal = productTotal + shippingCostTotal;

        return CheckoutInfo.builder()
                        .productCost(productCost)
                        .productTotal(productTotal)
                        .shippingCostTotal(shippingCostTotal)
                        .paymentTotal(paymentTotal)
                        .deliverDays(shippingRate.getDays())
                        .codSupported(shippingRate.isCodSupported())
                        .build();
    }

    private float calculatorShippingCost(List<CartItem> cartItems,
                                         ShippingRate shippingRate) {
        float shippingCostTotal = 0.0f;

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            float dimWeight = (product.getWeight() * product.getHeight()
                    * product.getWeight()) / DIM_DIVISOR;
            float finalWeight = product.getWeight() > dimWeight ?
                    product.getWeight() : dimWeight;

            float shippingCost = finalWeight
                    * item.getQuantity()
                    * shippingRate.getRate();

            shippingCostTotal += shippingCost;
        }
        return shippingCostTotal;
    }

    private float calculatorProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;

        for (CartItem item : cartItems) {
            float discountPrice = item.getProduct().getPrice()
                    * (1.0f - item.getProduct().getDiscountPercent());
            total += discountPrice * item.getQuantity();
        }
        return total;
    }

    private float calculatorProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;

        for (CartItem item : cartItems)
            cost += item.getQuantity() * item.getProduct().getCost();

        return cost;
    }
}
