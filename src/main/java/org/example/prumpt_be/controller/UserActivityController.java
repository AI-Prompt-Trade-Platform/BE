package org.example.prumpt_be.controller;

import org.example.prumpt_be.service.UserPromptService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 사용자의 활동 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/users/me/activity") // "me"를 사용하여 현재 인증된 사용자 관련임을 명시
@RequiredArgsConstructor
@Tag(name = "User Activity", description = "사용자 활동 (구매/판매 프롬프트, 거래 요약) 관련 API")
public class UserActivityController {

    private final UserPromptService userPromptService;

    //프로필 컨트롤러에 포함됨
//    @Operation(summary = "내가 구매한 프롬프트 목록 조회", description = "현재 사용자가 구매한 프롬프트 목록을 페이지네이션하여 조회합니다.")
//    @GetMapping("/prompts/purchased")
//    public ResponseEntity<PageResponseDto<PurchasedPromptDto>> getMyPurchasedPrompts(
//            @AuthenticationPrincipal Jwt jwt,
//            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=purchasedAt,desc")
//            @PageableDefault(size = 10, sort = "purchasedAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        // JWT로 유저 ID 조회
//        String auth0Id = jwt.getSubject(); // JWT에서 Auth0 ID 추출
//        PageResponseDto<PurchasedPromptDto> purchasedPrompts = userPromptService.getMyPurchasedPrompts(auth0Id, pageable);
//        return ResponseEntity.ok(purchasedPrompts);
//    }
//
//    @Operation(summary = "내가 판매중인 프롬프트 목록 조회", description = "현재 사용자가 판매 중인 프롬프트 목록을 페이지네이션하여 조회합니다.")
//    @GetMapping("/prompts/selling")
//    public ResponseEntity<PageResponseDto<SellingPromptDto>> getMySellingPrompts(
//            @AuthenticationPrincipal Jwt jwt,
//            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=createdAt,desc")
//            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
//        // JWT로 유저 ID 조회
//        String auth0Id = jwt.getSubject(); // JWT에서 Auth0 ID 추출
//        PageResponseDto<SellingPromptDto> sellingPrompts = userPromptService.getMySellingPrompts(auth0Id, pageable);
//        return ResponseEntity.ok(sellingPrompts);
//    }
//

}