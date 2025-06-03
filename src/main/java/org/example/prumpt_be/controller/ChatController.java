package org.example.prumpt_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.request.PromptUploadRequestDto;
import org.example.prumpt_be.service.AInspectionService;
import org.example.prumpt_be.service.OpenAiService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final OpenAiService openAiService;
    private final AInspectionService aInspectionService;

    // 프롬프트 평가 API
    @PostMapping("/prompts/upload")
    public void uploadPrompt(
            @RequestBody PromptUploadRequestDto request //평가에 필요한 정보 파라미터로 입력
    ) {
        aInspectionService.handlePromptUploadAndEvaluation(request);
    }


}
