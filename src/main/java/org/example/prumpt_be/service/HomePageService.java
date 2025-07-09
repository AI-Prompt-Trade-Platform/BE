package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.springframework.data.domain.Pageable;

/**
 * 홈 화면 및 프롬프트 목록 조회 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 인기 프롬프트, 최신 프롬프트, 프롬프트 검색, 카테고리별 필터링 기능을 제공합니다.
 */
public interface HomePageService {

    PageResponseDto<PromptSummaryDto> getPopularPrompts(Pageable pageable);

    PageResponseDto<PromptSummaryDto> getRecentPrompts(Pageable pageable);

    PageResponseDto<PromptSummaryDto> searchPrompts(String keyword, Pageable pageable);

    /**
     * 지정된 모델 카테고리 및/또는 타입 카테고리로 프롬프트를 필터링하여 페이지네이션된 결과를 반환합니다.
     *
     * @param modelCategorySlug 필터링할 모델 카테고리 슬러그 (선택 사항, null 가능)
     * @param typeCategorySlug 필터링할 타입 카테고리 슬러그 (선택 사항, null 가능)
     * @param pageable 페이지네이션 및 정렬 정보
     * @return 필터링된 프롬프트 목록 DTO
     */
    PageResponseDto<PromptSummaryDto> filterPromptsByCategories(
            String modelCategorySlug,
            String typeCategorySlug,
            Pageable pageable
    );
}