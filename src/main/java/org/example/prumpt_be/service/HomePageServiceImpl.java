package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.service.HomePageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // StringUtils 임포트

import lombok.RequiredArgsConstructor;

/**
 * HomePageService의 구현체입니다.
 * 홈 화면 및 프롬프트 목록 관련 데이터 조회 로직을 실제로 수행합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomePageServiceImpl implements HomePageService {

    private final PromptRepository promptRepository;

    @Override
    public PageResponseDto<PromptSummaryDto> getPopularPrompts(Pageable pageable) {
        Page<Prompt> popularPromptsPage = promptRepository.findAll(pageable);
        return new PageResponseDto<>(popularPromptsPage.map(this::convertToPromptSummaryDto));
    }

    @Override
    public PageResponseDto<PromptSummaryDto> getRecentPrompts(Pageable pageable) {
        Page<Prompt> recentPromptsPage = promptRepository.findAll(pageable);
        return new PageResponseDto<>(recentPromptsPage.map(this::convertToPromptSummaryDto));
    }

    @Override
    public PageResponseDto<PromptSummaryDto> searchPrompts(String keyword, Pageable pageable) {
        // StringUtils.hasText를 사용하여 null, 빈 문자열, 공백만 있는 문자열을 모두 체크
        if (!StringUtils.hasText(keyword)) {
            Page<Prompt> emptyKeywordPromptsPage = promptRepository.findAll(pageable);
            return new PageResponseDto<>(emptyKeywordPromptsPage.map(this::convertToPromptSummaryDto));
        }
        Page<Prompt> searchedPromptsPage = promptRepository.findByKeyword(keyword.trim(), pageable);
        return new PageResponseDto<>(searchedPromptsPage.map(this::convertToPromptSummaryDto));
    }

    @Override
    public PageResponseDto<PromptSummaryDto> filterPromptsByCategories(
            String modelCategorySlug,
            String typeCategorySlug,
            Pageable pageable) {

        // 슬러그 값이 비어있거나 공백만 있으면 null로 처리하여 Repository에서 조건 무시하도록 함
        String finalModelSlug = StringUtils.hasText(modelCategorySlug) ? modelCategorySlug.trim() : null;
        String finalTypeSlug = StringUtils.hasText(typeCategorySlug) ? typeCategorySlug.trim() : null;

        Page<Prompt> filteredPromptsPage = promptRepository.findPromptsByCategories(
                finalModelSlug,
                finalTypeSlug,
                pageable
        );
        return new PageResponseDto<>(filteredPromptsPage.map(this::convertToPromptSummaryDto));
    }

    // --- Helper Methods ---
    private PromptSummaryDto convertToPromptSummaryDto(Prompt prompt) {
        if (prompt == null) {
            return null;
        }
        String ownerName = (prompt.getOwner() != null && prompt.getOwner().getProfileName() != null)
                ? prompt.getOwner().getProfileName()
                : "Unknown";

        return PromptSummaryDto.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .ownerProfileName(ownerName)
                .thumbnailImageUrl(prompt.getExampleContentUrl())
                .aiInspectionRate(prompt.getAiInspectionRate())
                .createdAt(prompt.getCreatedAt())
                .build();
    }
}