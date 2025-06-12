package org.example.prumpt_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

import org.example.prumpt_be.dto.request.TossConfirmRequestDto;
import org.example.prumpt_be.dto.request.PointPurchaseRequest;
import org.example.prumpt_be.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentWidgetController {

    private final UserService userService;

    @Value("${toss.secret-key}")
    private String tossSecretKey;


    @Operation(summary = "결제요청 처리", description = "auth0Id 를 받아 결제 처리.")
    @GetMapping("/api/payments/confirm")
    public ResponseEntity<?> confirmPayment(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount,
            @RequestParam Long userId
    ) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // tossSecretKey는 application.properties에 있어야 함
        String encodedAuth = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        Map<String, Object> payload = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                userService.addPointToUser(userId, amount);
                return ResponseEntity.ok(amount + "포인트 지급 성공");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제 검증 실패");
            }
        } catch (Exception e) {
            log.error("결제 확인 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 처리 중 오류 발생");
        }
    }

}
