package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.request.UserProfileUpdateDto;
import org.example.prumpt_be.dto.response.UserProfileDto;
import org.example.prumpt_be.service.UserProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.security.core.annotation.AuthenticationPrincipal; // Spring Security 사용 시

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 프로필 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 * 프로필 조회 및 수정을 담당합니다.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "사용자 프로필 관련 API")
public class UserProfileController {

    private final UserProfileService userProfileService;

    // 임시로 Auth0 ID를 헤더에서 받는다고 가정. 실제로는 Spring Security의 AuthenticationPrincipal 사용 권장.
    private static final String AUTH0_ID_HEADER = "X-Auth0-Id"; 

    @Operation(summary = "현재 사용자 프로필 조회", description = "현재 인증된 사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/me/profile")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(
            @Parameter(description = "인증된 사용자의 Auth0 ID (실제로는 토큰에서 추출)", required = true)
            @RequestHeader(AUTH0_ID_HEADER) String auth0Id) {
        // String auth0Id = principal.getName(); // @AuthenticationPrincipal CustomUserDetails principal 형태일 때
        UserProfileDto userProfile = userProfileService.getCurrentUserProfile(auth0Id);
        return ResponseEntity.ok(userProfile);
    }

    @Operation(summary = "현재 사용자 프로필 수정", description = "현재 인증된 사용자의 프로필 정보를 수정합니다.")
    @PutMapping("/me/profile")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
            @Parameter(description = "인증된 사용자의 Auth0 ID (실제로는 토큰에서 추출)", required = true)
            @RequestHeader(AUTH0_ID_HEADER) String auth0Id,
            @RequestBody UserProfileUpdateDto userProfileUpdateDto) {
        UserProfileDto updatedProfile = userProfileService.updateCurrentUserProfile(auth0Id, userProfileUpdateDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @Operation(summary = "특정 사용자 프로필 조회 (공개용)", description = "ID로 특정 사용자의 프로필 정보를 조회합니다 (공개 가능한 정보만).")
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @Parameter(description = "조회할 사용자의 ID") @PathVariable Long userId) {
        UserProfileDto userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }
}