package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto; // 홈 화면 등에서 사용하는 DTO 재활용 가능
import org.springframework.data.domain.Pageable;

/**
 * 사용자 위시리스트 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 위시리스트에 프롬프트를 추가, 삭제하고 사용자의 위시리스트를 조회하는 기능을 제공합니다.
 */
public interface WishlistService {

    /**
     * 현재 인증된 사용자의 위시리스트에 특정 프롬프트를 추가합니다.
     * 이미 위시리스트에 있는 프롬프트는 중복 추가하지 않습니다.
     *
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
     * @param promptId 위시리스트에 추가할 프롬프트의 ID
     * @throws RuntimeException 사용자를 찾을 수 없거나 프롬프트를 찾을 수 없는 경우 발생 (적절한 예외로 변경 권장)
     */
    void addPromptToWishlist(String auth0Id, Long promptId);

    /**
     * 현재 인증된 사용자의 위시리스트에서 특정 프롬프트를 제거합니다.
     *
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
     * @param promptId 위시리스트에서 제거할 프롬프트의 ID
     * @throws RuntimeException 사용자를 찾을 수 없거나, 프롬프트를 찾을 수 없거나, 위시리스트에 해당 항목이 없는 경우 발생 (적절한 예외로 변경 권장)
     */
//    void removePromptFromWishlist(String auth0Id, Long promptId);

    /**
     * 현재 인증된 사용자의 위시리스트 목록을 페이지네이션하여 조회합니다.
     *
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
     * @param pageable 페이지네이션 정보 (정렬 기준: addedAt DESC)
     * @return 페이징된 위시리스트 프롬프트 목록 (PromptSummaryDto 사용)
     * @throws RuntimeException 사용자를 찾을 수 없는 경우 발생 (적절한 예외로 변경 권장)
     */
    PageResponseDto<PromptSummaryDto> getUserWishlist(String auth0Id, Pageable pageable);

    /**
     * 현재 인증된 사용자의 위시리스트에 특정 프롬프트가 있는지 확인합니다.
     *
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
     * @param promptId 확인할 프롬프트의 ID
     * @return 위시리스트에 해당 프롬프트가 있으면 true, 없으면 false
     * @throws RuntimeException 사용자를 찾을 수 없거나 프롬프트를 찾을 수 없는 경우 발생 (적절한 예외로 변경 권장)
     */
    boolean isPromptInWishlist(String auth0Id, Long promptId);
}