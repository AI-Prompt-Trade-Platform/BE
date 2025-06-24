package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.dto.request.UserProfileUpdateDto;
import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PurchasedPromptDto;
import org.example.prumpt_be.dto.response.UserProfileDto;
import org.example.prumpt_be.service.UserProfileService;
import org.example.prumpt_be.service.UserPromptService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 사용자 프로필 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 * 프로필 조회 및 수정을 담당합니다.
 * todo: 사용자 프로필 관련 API 컨트롤러 (필수)
 */
@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "사용자 프로필 관련 API")
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserPromptService userPromptService;


    @Operation(summary = "현재 사용자 프로필 조회", description = "현재 인증된 사용자의 프로필 정보를 조회합니다.")
    @GetMapping("/profile") // todo: 추가로 판매중인 프롬프트 목록까지 조회 하게 수정 필요
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(
            @AuthenticationPrincipal Jwt jwt) {
        // JWT로 유저 ID 조회
        String auth0Id = jwt.getSubject(); // JWT에서 Auth0 ID 추출
        UserProfileDto userProfile = userProfileService.getCurrentUserProfile(auth0Id);
        return ResponseEntity.ok(userProfile);
    }

    @Operation(summary = "현재 사용자 프로필 수정", description = "현재 인증된 사용자의 프로필 정보를 수정합니다.")
    @PutMapping("/me/profile/update")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(
            @Parameter(description = "인증된 사용자의 Auth0 ID (실제로는 토큰에서 추출)", required = true)
            @AuthenticationPrincipal Jwt jwt,
            @ModelAttribute UserProfileUpdateDto userProfileUpdateDto) {
        // JWT로 유저 ID 조회
        String auth0Id = jwt.getSubject(); // JWT에서 Auth0 ID 추출
        UserProfileDto updatedProfile = userProfileService.updateCurrentUserProfile(auth0Id, userProfileUpdateDto);
        return ResponseEntity.ok(updatedProfile);
    }

    @Operation(summary = "특정 사용자 프로필 조회 (공개용)", description = "ID로 특정 사용자의 프로필 정보를 조회합니다 (공개 가능한 정보만).")
    @GetMapping("/profile/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfile(
            @Parameter(description = "위시리스트에 추가할 프롬프트의 ID") @PathVariable String userId) {
        UserProfileDto userProfile = userProfileService.getUserProfile(userId);
        return ResponseEntity.ok(userProfile);
    }

    @Operation(summary = "내가 구매한 프롬프트 목록 조회", description = "현재 사용자가 구매한 프롬프트 목록을 페이지네이션하여 조회합니다.")
    @GetMapping("/prompts/purchased")
    public ResponseEntity<PageResponseDto<PurchasedPromptDto>> getMyPurchasedPrompts(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=purchasedAt,desc")
            @PageableDefault(size = 10, sort = "purchasedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // JWT로 유저 ID 조회
        String auth0Id = jwt.getSubject(); // JWT에서 Auth0 ID 추출
        PageResponseDto<PurchasedPromptDto> purchasedPrompts = userPromptService.getMyPurchasedPrompts(auth0Id, pageable);
        return ResponseEntity.ok(purchasedPrompts);
    }

    @Operation(summary = "내가 판매중인 프롬프트 목록 조회", description = "현재 사용자가 판매 중인 프롬프트의 상세 정보 목록을 페이지네이션하여 조회합니다.")
    @GetMapping("/prompts/selling")
    public ResponseEntity<PageResponseDto<PromptDetailDTO>> getMySellingPrompts(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=createdAt,desc")
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // JWT로 유저 ID 조회
        String auth0Id = jwt.getSubject(); // JWT에서 Auth0 ID 추출
        PageResponseDto<PromptDetailDTO> sellingPrompts = userPromptService.getMySellingPrompts(auth0Id, pageable);
        return ResponseEntity.ok(sellingPrompts);
    }
}