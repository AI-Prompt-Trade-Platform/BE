package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.request.UserProfileUpdateDto;
import org.example.prumpt_be.dto.response.UserProfileDto;
import org.example.prumpt_be.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
// import org.springframework.security.core.context.SecurityContextHolder; // Spring Security 사용 시
// import org.springframework.security.core.userdetails.UsernameNotFoundException; // 적절한 예외 사용

/**
 * UserProfileService의 구현체입니다.
 * 사용자 프로필 정보 조회 및 수정 로직을 실제로 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(String auth0Id) {
        Users user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found with auth0id: " + auth0Id)); // TODO: 적절한 예외 클래스 사용
        return convertToUserProfileDto(user);
    }
    
    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile(String auth0Id) {
        // String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName(); // Spring Security 사용 시
        Users user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found with auth0Id: " + auth0Id)); // TODO: UserNotFoundException 등
        return convertToUserProfileDto(user);
    }

    @Override
    @Transactional // 데이터 변경이 있으므로 readOnly = false (기본값)
    public UserProfileDto updateCurrentUserProfile(String auth0Id, UserProfileUpdateDto updateDto) {
        Users user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found with auth0Id: " + auth0Id));

        // DTO의 필드를 사용하여 User 엔티티 업데이트
        if (updateDto.getProfileName() != null && !updateDto.getProfileName().isBlank()) {
            user.setProfileName(updateDto.getProfileName());
        }
        if (updateDto.getIntroduction() != null) { // 자기소개는 비워둘 수도 있음
            user.setIntroduction(updateDto.getIntroduction());
        }
        if (updateDto.getProfileImgUrl() != null) {
            user.setProfileImg_url(updateDto.getProfileImgUrl());
        }
        if (updateDto.getBannerImgUrl() != null) {
            user.setBannerImg_url(updateDto.getBannerImgUrl());
        }
        // user.setUpdatedAt(LocalDateTime.now()); // @UpdateTimestamp 사용 시 자동 업데이트

        Users updatedUser = userRepository.save(user);
        return convertToUserProfileDto(updatedUser);
    }

    // --- Helper Methods ---
    private UserProfileDto convertToUserProfileDto(Users user) {
        return UserProfileDto.builder()
                .userId(user.getUserId())
                .profileName(user.getProfileName())
                .introduction(user.getIntroduction())
                .profileImgUrl(user.getProfileImg_url())
                .bannerImgUrl(user.getBannerImg_url())
                .point(user.getPoint())
                .build();
    }
}