package org.example.prumpt_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.request.PromptUploadRequestDto;
import org.example.prumpt_be.service.AInspectionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluate")
public class EvaluateController {
    private final AInspectionService aInspectionService;

    // 프롬프트 평가 API
    @Operation(summary = "AI에 프롬프트 평가 요청", description = "ID와 프롬프트 내용, 타입을 입력받아 AI에 평가를 요청합니다.")
    @PostMapping("/prompts/upload")
    public void uploadPrompt(
            @RequestBody PromptUploadRequestDto request
    ) {
        aInspectionService.handlePromptUploadAndEvaluation(request);
    }
}
