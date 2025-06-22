package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto; // 또는 Wishlist용 별도 DTO
import org.example.prumpt_be.service.WishlistService;
import org.example.prumpt_be.service.WishlistServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 사용자 위시리스트 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 * todo: 위시리스트 기능 컨트롤러 (필수)
 */
@RestController
@RequestMapping("/api/wishlist") // 기본 경로를 /api/users/me/wishlist 로 설정
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "사용자 위시리스트 관련 API")
public class WishlistController {

//    private final WishlistService wishlistService;
    private final WishlistServiceImpl wishlistServiceImpl;

    //todo: 추가/삭제 토글로 구현 필요
    @Operation(summary = "위시리스트에 프롬프트 추가", description = "현재 사용자의 위시리스트에 특정 프롬프트를 추가합니다.")
    @PostMapping("/{promptId}") // 경로 변수로 promptId를 받음
    public ResponseEntity<Void> addPromptToWishlist(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true)
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "위시리스트에 추가할 프롬프트의 ID") @PathVariable Long promptId) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        wishlistServiceImpl.addPromptToWishlist(userAuth0Id, promptId);
        // 성공 시 201 Created 또는 204 No Content 반환 가능
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "내 위시리스트 목록 조회", description = "현재 사용자의 위시리스트에 담긴 프롬프트 목록을 페이지네이션하여 조회합니다.")
    @GetMapping
    public ResponseEntity<PageResponseDto<PromptSummaryDto>> getUserWishlist(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true)
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=addedAt,DESC")
            // PromptSummaryDto 대신 Wishlist 항목에 특화된 DTO를 만들어서 사용할 수도 있습니다.
            @PageableDefault(size = 10, sort = "addedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        PageResponseDto<PromptSummaryDto> wishlist = wishlistServiceImpl.getUserWishlist(userAuth0Id, pageable);
        return ResponseEntity.ok(wishlist);
    }

    // 필요한가?
    @Operation(summary = "프롬프트 위시리스트 포함 여부 확인", description = "현재 사용자의 위시리스트에 특정 프롬프트가 포함되어 있는지 확인합니다.")
    @GetMapping("/{promptId}/exists")
    public ResponseEntity<Boolean> isPromptInWishlist(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true)
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "확인할 프롬프트의 ID") @PathVariable Long promptId) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        boolean isInWishlist = wishlistServiceImpl.isPromptInWishlist(userAuth0Id, promptId);
        return ResponseEntity.ok(isInWishlist);
    }
}