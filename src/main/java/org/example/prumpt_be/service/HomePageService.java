package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.springframework.data.domain.Pageable;

/**
 * 홈 화면에 필요한 데이터 조회 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 인기 프롬프트, 최신 프롬프트, 프롬프트 검색 기능을 제공합니다.
 */
public interface HomePageService {

    /**
     * 인기있는 프롬프트 목록을 페이지네이션하여 조회합니다.
     * (현재는 임시로 가격 높은 순으로 조회합니다. 실제 인기 로직은 구체화 필요)
     * @param pageable 페이지네이션 정보 (컨트롤러에서 전달받음)
     * @return 페이징된 인기 프롬프트 목록
     */
    PageResponseDto<PromptSummaryDto> getPopularPrompts(Pageable pageable);

    /**
     * 최근 등록된 프롬프트 목록을 페이지네이션하여 조회합니다.
     * @param pageable 페이지네이션 정보 (컨트롤러에서 전달받음)
     * @return 페이징된 최신 프롬프트 목록
     */
    PageResponseDto<PromptSummaryDto> getRecentPrompts(Pageable pageable);

    /**
     * 키워드로 프롬프트를 검색하여 페이지네이션된 결과를 반환합니다.
     * @param keyword 검색어
     * @param pageable 페이지네이션 정보 (컨트롤러에서 전달받음)
     * @return 페이징된 검색 결과 프롬프트 목록
     */
    PageResponseDto<PromptSummaryDto> searchPrompts(String keyword, Pageable pageable);
}