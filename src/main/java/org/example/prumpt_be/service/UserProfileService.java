package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.response.UserMypageResponse;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;

    public UserMypageResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMypageResponse.builder()
                .userId(user.getUserId())
                .profileName(user.getProfileName())
                .profileImgUrl(user.getProfileImgUrl())
                .bannerImgUrl(user.getBannerImgUrl())
                .introduction(user.getIntroduction())
                .userRole(user.getUserRole())
                .point(user.getPoint())
                .build();
    }
}