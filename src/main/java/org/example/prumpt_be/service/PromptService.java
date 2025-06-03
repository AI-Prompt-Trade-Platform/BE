package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.response.PromptListResponse;
import org.example.prumpt_be.dto.response.PromptSummary;
import org.example.prumpt_be.repository.PromptRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptRepository promptRepository;

    public PromptListResponse getPromptsByCategoryType(String type) {
        List<Prompt> prompts = promptRepository.findByType(type);
        return toResponse(prompts);
    }

    public PromptListResponse getPromptsByModel(String model) {
        List<Prompt> prompts = promptRepository.findByModel(model);
        return toResponse(prompts);
    }

    public PromptListResponse searchPrompts(String keyword) {
        List<Prompt> prompts = promptRepository.findByPromptNameContainingIgnoreCase(keyword);
        return toResponse(prompts);
    }

    // 프롬프트 상세화면 정보 반환
    private PromptListResponse toResponse(List<Prompt> prompts) {
        List<PromptSummary> list = prompts.stream()
                .map(p -> PromptSummary.builder()
                        .promptId(p.getPromptId())
                        .promptName(p.getPromptName())
                        .price(p.getPrice())
                        .aiInspectionRate(p.getAiInspectionRate())
                        .exampleContentUrl(p.getExampleContentUrl())
                        .build())
                .collect(Collectors.toList());
        return new PromptListResponse(list);
    }
}