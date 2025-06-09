package org.example.prumpt_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.request.PromptPurchaseRequest;
import org.example.prumpt_be.service.PromptTradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prompts")
@RequiredArgsConstructor
public class PromptTradeController {

    private final PromptTradeService promptTradeService;

    @PostMapping("/{promptId}/purchase")
    public ResponseEntity<String> purchasePrompt(@PathVariable Integer promptId, @RequestBody PromptPurchaseRequest request) {
        promptTradeService.purchasePrompt(promptId, request.getBuyerId());
        return ResponseEntity.ok("프롬프트 구매 성공");
    }
}
