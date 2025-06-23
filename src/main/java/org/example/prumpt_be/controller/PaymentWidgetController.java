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
import org.example.prumpt_be.service.UserService;

@Slf4j
@RestController
@RequestMapping("/api/payments") // <-- 경로를 @RequestMapping으로 분리합니다.
@RequiredArgsConstructor
public class PaymentWidgetController {

    private final UserService userService;

    @Value("${toss.secret-key}")
    private String tossSecretKey;


    @Operation(summary = "결제요청 처리", description = "auth0Id 를 받아 결제 처리.")
    @PostMapping("/confirm") // <-- 결제 '확인'은 POST 요청이 더 적합합니다.
    public ResponseEntity<?> confirmPayment(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            @RequestParam int amount,
            @AuthenticationPrincipal Jwt jwt
    ) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String encodedAuth = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        headers.set("Authorization", "Basic " + encodedAuth);

        Map<String, Object> payload = Map.of(
                "paymentKey", paymentKey,
                "orderId", orderId,
                "amount", amount
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

            // 응답 상태 코드로 확인
            if (response.getStatusCode() == HttpStatus.OK) {
                // 성공 시 포인트 충전
                userService.addPointToUser(userAuth0Id, amount);
                return ResponseEntity.ok(Map.of("message", amount + "포인트 충전 성공", "data", response.getBody()));
            } else {
                // 실패 시 토스페이먼츠의 응답을 그대로 전달
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (Exception e) {
            log.error("결제 확인 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "결제 처리 중 오류 발생"));
        }
    }
}