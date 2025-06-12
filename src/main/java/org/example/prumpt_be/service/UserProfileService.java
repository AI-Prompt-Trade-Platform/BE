package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.request.UserProfileUpdateDto;
import org.example.prumpt_be.dto.response.UserProfileDto;

/**
 * 사용자 프로필 정보 조회 및 수정 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface UserProfileService {

    /**
     * 특정 사용자의 프로필 정보를 조회합니다.
     * @param userId 조회할 사용자 ID
     * @return 사용자 프로필 정보 DTO
     */
    UserProfileDto getUserProfile(String userId);

    /**
     * 현재 인증된 사용자의 프로필 정보를 조회합니다.
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
     * @return 사용자 프로필 정보 DTO
     */
    UserProfileDto getCurrentUserProfile(String auth0Id); // 인증 정보에서 사용자 식별

    /**
     * 현재 인증된 사용자의 프로필 정보를 수정합니다.
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID
     * @param userProfileUpdateDto 수정할 프로필 정보
     * @return 수정된 사용자 프로필 정보 DTO
     */
    UserProfileDto updateCurrentUserProfile(String auth0Id, UserProfileUpdateDto userProfileUpdateDto);
}