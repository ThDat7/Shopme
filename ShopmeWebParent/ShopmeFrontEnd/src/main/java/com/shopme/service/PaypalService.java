package com.shopme.service;

import com.shopme.common.setting_bag.PaymentSettingBag;
import com.shopme.exception.PaypalPaymentException;
import com.shopme.payload.response.PaypalOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class PaypalService {
    private static final String GET_ORDER_API = "/v2/checkout/orders/";

    @Autowired
    private SettingService settingService;

    public void validateOrder(String orderId) {
        PaypalOrderResponse paypalOrderResponse = getOrderDetails(orderId);

        if (orderId != paypalOrderResponse.getOrderId() ||
                !paypalOrderResponse.getStatus()
                .equals("COMPLETED"))
            throw new PaypalPaymentException("Order information is invalid");
    }

    private PaypalOrderResponse getOrderDetails(String orderId) {
        ResponseEntity<PaypalOrderResponse> response = makeRequest(orderId);

        if (!response.getStatusCode().equals(HttpStatus.OK))
            throwPaypalExceptionForNoneOkResponse(response.getStatusCode());
        return response.getBody();
    }

    private ResponseEntity<PaypalOrderResponse> makeRequest(String orderId) {
        PaymentSettingBag paymentSettingBag =
                new PaymentSettingBag(settingService.getPaymentSettings());
        String baseURL = paymentSettingBag.getURL();
        String requestURL = baseURL + GET_ORDER_API + orderId;

        String clientId = paymentSettingBag.getClientId();
        String clientSecret = paymentSettingBag.getClientSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.add("Accept-Language", "en_US");
        headers.setBasicAuth(clientId, clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .exchange(requestURL, HttpMethod.POST, request, PaypalOrderResponse.class);
    }

    private void throwPaypalExceptionForNoneOkResponse(HttpStatus httpStatus) {
        String messageError = "";

        switch (httpStatus) {
            case NOT_FOUND:{
                messageError = "Order id not found.";
                break;
            }

            case BAD_REQUEST: {
                messageError = "Bad Request to PayPal Checkout API";
                break;
            }

            case INTERNAL_SERVER_ERROR: {
                messageError = "PayPal server error";
                break;
            }

            default: messageError = "PayPal returned non-OK status code";
        }

        throw new PaypalPaymentException(messageError);
    }
}
