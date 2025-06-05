package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PurchasedPromptDto;
import org.example.prumpt_be.dto.response.SellingPromptDto;
import org.springframework.data.domain.Pageable;

/**
 * 사용자의 프롬프트 활동(구매한 프롬프트, 판매 중인 프롬프트) 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface UserPromptService {

    /**
     * 현재 인증된 사용자가 구매한 프롬프트 목록을 페이지네이션하여 조회합니다.
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
     * @param pageable 페이지네이션 정보
     * @return 페이징된 구매한 프롬프트 목록
     */
    PageResponseDto<PurchasedPromptDto> getMyPurchasedPrompts(String auth0Id, Pageable pageable);

    /**
     * 현재 인증된 사용자가 판매 중인 프롬프트 목록을 페이지네이션하여 조회합니다.
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
     * @param pageable 페이지네이션 정보
     * @return 페이징된 판매 중인 프롬프트 목록
     */
    PageResponseDto<SellingPromptDto> getMySellingPrompts(String auth0Id, Pageable pageable);
}