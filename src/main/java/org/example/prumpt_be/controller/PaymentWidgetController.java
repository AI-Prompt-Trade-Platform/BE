package org.example.prumpt_be.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.prumpt_be.service.PromptTradeService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentWidgetController {

    private final PromptTradeService promptTradeService;

    @Value("${toss.secret-key}")
    private String tossSecretKey;

    // 결제 요청을 처리하는 메서드
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody String jsonBody) throws Exception {
        // JWT에서 사용자 ID 추출
        String userAuth0Id = jwt.getSubject();
        JSONParser parser = new JSONParser();
        JSONObject result;
        String orderId;
        int amount;
        int promptId;
        int buyerId;
        String paymentKey;

        try {
            JSONObject data = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) data.get("paymentKey");
            orderId = (String) data.get("orderId");
            amount = Integer.parseInt((String) data.get("amount"));
            promptId = Integer.parseInt((String) data.get("promptId"));
            buyerId = Integer.parseInt((String) data.get("buyerId"));
        } catch (ParseException | NumberFormatException e) {
            throw new IllegalArgumentException("결제 요청 파라미터가 잘못되었습니다", e);
        }

        // Toss 승인 API 요청 바디 구성
        JSONObject tossRequest = new JSONObject();
        tossRequest.put("paymentKey", paymentKey);
        tossRequest.put("orderId", orderId);
        tossRequest.put("amount", amount);

        // Authorization 헤더 구성
        String encodedAuth = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorization = "Basic " + encodedAuth;

        // Toss API 호출
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            os.write(tossRequest.toJSONString().getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = connection.getResponseCode();
        InputStream is = (responseCode == 200) ? connection.getInputStream() : connection.getErrorStream();
        Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
        result = (JSONObject) parser.parse(reader);

        if (responseCode == 200) {
            // 결제 성공 시 구매 처리
            promptTradeService.purchasePrompt(promptId, userAuth0Id);

            JSONObject success = new JSONObject();
            success.put("message", "결제 및 프롬프트 구매 성공");
            success.put("buyerId", buyerId);
            success.put("promptId", promptId);
            return ResponseEntity.ok(success);
        } else {
            return ResponseEntity.status(400).body(result);
        }
    }
}
