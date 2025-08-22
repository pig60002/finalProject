package com.bank.creditCard.transaction.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PayPalApiClient {

    @Value("${paypal.mode:sandbox}")
    private String mode;

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String secret;

    private final RestTemplate rest = new RestTemplate();

    private String base() {
        // sandbox: https://api-m.sandbox.paypal.com
        // live:    https://api-m.paypal.com
        return "https://api-m." + ("live".equalsIgnoreCase(mode) ? "" : "sandbox.") + "paypal.com";
    }

    private String getAccessToken() {
        String url = base() + "/v1/oauth2/token";
        HttpHeaders h = new HttpHeaders();
        h.setBasicAuth(clientId, secret);
        h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> req = new HttpEntity<>("grant_type=client_credentials", h);
        Map<?, ?> body = rest.postForObject(url, req, Map.class);
        return (String) body.get("access_token");
    }

    /** Card Fields 必須的 client token */
    public String generateClientToken() {
        String url = base() + "/v1/identity/generate-token";
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(getAccessToken());
        h.setContentType(MediaType.APPLICATION_JSON);
        Map<?, ?> resp = rest.postForObject(url, new HttpEntity<>(Map.of(), h), Map.class);
        return (String) resp.get("client_token");
    }

    /** 建單（intent=CAPTURE）。TWD 為零小數，value請傳「整數字串」，例如"1000" */
    public String createOrder(String currency, String value) {
        String url = base() + "/v2/checkout/orders";
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(getAccessToken());
        h.setContentType(MediaType.APPLICATION_JSON);
        // 冪等鍵避免重送
        h.add("PayPal-Request-Id", "create-" + UUID.randomUUID());

        Map<String, Object> payload = Map.of(
                "intent", "CAPTURE",
                "purchase_units", List.of(
                        Map.of("amount", Map.of("currency_code", currency, "value", value))
                ),
                "application_context", Map.of("shipping_preference", "NO_SHIPPING")
        );

        Map<?, ?> resp = rest.postForObject(url, new HttpEntity<>(payload, h), Map.class);
        return (String) resp.get("id");
    }

    /** 扣款（capture） */
    public Map<String, Object> captureOrder(String orderId) {
        String url = base() + "/v2/checkout/orders/" + orderId + "/capture";
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(getAccessToken());
        h.setContentType(MediaType.APPLICATION_JSON);
        h.add("PayPal-Request-Id", "capture-" + orderId);

        ResponseEntity<Map> resp = rest.postForEntity(url, new HttpEntity<>(h), Map.class);
        // 直接把 PayPal 的回應轉交出去
        return resp.getBody();
    }
}
