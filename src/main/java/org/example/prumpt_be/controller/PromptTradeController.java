package org.example.prumpt_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.request.PromptPurchaseRequest;
import org.example.prumpt_be.service.PromptTradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prompts")
@RequiredArgsConstructor
public class PromptTradeController {

    private final PromptTradeService promptTradeService;

    @Operation(summary = "프롬프트 구매 API", description = "프롬프트ID 를 입력받아 구매, 포인트 차감.")
    @PostMapping("/{promptId}/purchase")
    public ResponseEntity<String> purchasePrompt(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Integer promptId) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        promptTradeService.purchasePrompt(promptId, userAuth0Id);
        return ResponseEntity.ok("프롬프트 구매 성공");
    }
}
