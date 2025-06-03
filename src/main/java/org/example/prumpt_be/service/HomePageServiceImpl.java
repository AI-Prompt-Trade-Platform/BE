package org.example.prumpt_be.service.impl;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.service.HomePageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * HomePageService의 구현체입니다.
 * 홈 화면 관련 데이터 조회 로직을 실제로 수행합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 대부분 조회이므로 클래스 레벨에 읽기 전용 트랜잭션 적용
public class HomePageServiceImpl implements HomePageService {

    private final PromptRepository promptRepository;

    @Override
    public PageResponseDto<PromptSummaryDto> getPopularPrompts(Pageable pageable) {
        // TODO: "인기" 프롬프트에 대한 기준을 명확히 하고 그에 맞는 조회 로직 구현 필요.
        //       (예: 판매량, 조회수, 좋아요 수 등)
        //       현재는 임시로 PromptRepository에 정의된 findAllByOrderByPriceDesc를 사용합니다.
        //       만약 해당 메소드가 없다면, 예를 들어 findAll(pageable) 등으로 대체하거나 새로 정의해야 합니다.
        //       컨트롤러에서 sort = "price", direction = Sort.Direction.DESC 로 Pageable이 넘어오므로,
        //       별도의 정렬 메소드 없이 promptRepository.findAll(pageable)를 사용해도 됩니다.
        Page<Prompt> popularPromptsPage = promptRepository.findAll(pageable); // Pageable에 정렬 정보 포함
        return new PageResponseDto<>(popularPromptsPage.map(this::convertToPromptSummaryDto));
    }

    @Override
    public PageResponseDto<PromptSummaryDto> getRecentPrompts(Pageable pageable) {
        // 컨트롤러에서 sort = "createdAt", direction = Sort.Direction.DESC 로 Pageable이 넘어오므로,
        // promptRepository.findAll(pageable)를 사용하면 해당 정렬이 적용됩니다.
        // 또는, 레포지토리 메소드명으로 정렬을 명시할 수도 있습니다 (예: findAllByOrderByCreatedAtDesc).
        Page<Prompt> recentPromptsPage = promptRepository.findAll(pageable); // Pageable에 정렬 정보 포함
        return new PageResponseDto<>(recentPromptsPage.map(this::convertToPromptSummaryDto));
    }

    @Override
    public PageResponseDto<PromptSummaryDto> searchPrompts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            // 검색어가 없는 경우, 최신 프롬프트를 반환하거나 빈 결과를 반환할 수 있습니다.
            // 여기서는 최신 프롬프트를 반환하도록 (Pageable에 담긴 기본 정렬 사용)
            Page<Prompt> emptyKeywordPromptsPage = promptRepository.findAll(pageable);
            return new PageResponseDto<>(emptyKeywordPromptsPage.map(this::convertToPromptSummaryDto));
        }
        Page<Prompt> searchedPromptsPage = promptRepository.findByKeyword(keyword, pageable);
        return new PageResponseDto<>(searchedPromptsPage.map(this::convertToPromptSummaryDto));
    }

    // --- Helper Methods ---
    // Prompt 엔티티를 PromptSummaryDto로 변환하는 헬퍼 메소드
    // 여러 서비스에서 중복 사용될 가능성이 있다면 공통 유틸리티 클래스로 분리하는 것이 좋습니다.
    private PromptSummaryDto convertToPromptSummaryDto(Prompt prompt) {
        if (prompt == null) {
            return null;
        }
        String ownerName = (prompt.getOwner() != null && prompt.getOwner().getProfileName() != null)
                ? prompt.getOwner().getProfileName()
                : "Unknown"; // 또는 다른 기본값

        return PromptSummaryDto.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .ownerProfileName(ownerName)
                .thumbnailImageUrl(prompt.getExampleContentUrl()) // 예시 콘텐츠 URL을 썸네일로 사용
                .aiInspectionRate(prompt.getAiInspectionRate())
                .createdAt(prompt.getCreatedAt())
                .build();
    }
}