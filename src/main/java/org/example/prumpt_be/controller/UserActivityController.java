package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PurchasedPromptDto;
import org.example.prumpt_be.dto.response.SellingPromptDto;
import org.example.prumpt_be.service.UserPromptService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
//    private final TransactionService transactionService;

    // 임시로 Auth0 ID를 헤더에서 받는다고 가정.
    private static final String AUTH0_ID_HEADER = "X-Auth0-Id";

    @Operation(summary = "내가 구매한 프롬프트 목록 조회", description = "현재 사용자가 구매한 프롬프트 목록을 페이지네이션하여 조회합니다.")
    @GetMapping("/prompts/purchased")
    public ResponseEntity<PageResponseDto<PurchasedPromptDto>> getMyPurchasedPrompts(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true) @RequestHeader(AUTH0_ID_HEADER) String auth0Id,
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=purchasedAt,desc")
            @PageableDefault(size = 10, sort = "purchasedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<PurchasedPromptDto> purchasedPrompts = userPromptService.getMyPurchasedPrompts(auth0Id, pageable);
        return ResponseEntity.ok(purchasedPrompts);
    }

    @Operation(summary = "내가 판매중인 프롬프트 목록 조회", description = "현재 사용자가 판매 중인 프롬프트 목록을 페이지네이션하여 조회합니다.")
    @GetMapping("/prompts/selling")
    public ResponseEntity<PageResponseDto<SellingPromptDto>> getMySellingPrompts(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true) @RequestHeader(AUTH0_ID_HEADER) String auth0Id,
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=createdAt,desc")
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<SellingPromptDto> sellingPrompts = userPromptService.getMySellingPrompts(auth0Id, pageable);
        return ResponseEntity.ok(sellingPrompts);
    }



    //todo: 거래요약페이지 (내가 만든거랑 겹침)
//    @Operation(summary = "나의 거래 요약 조회", description = "현재 사용자의 특정 기간 동안의 거래 요약(일별 판매 건수, 수익)을 조회합니다.")
//    @GetMapping("/transactions/summary")
//    public ResponseEntity<List<TransactionSummaryItemDto>> getMyTransactionSummary(
//            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true) @RequestHeader(AUTH0_ID_HEADER) String auth0Id,
//            @Parameter(description = "조회 시작일 (YYYY-MM-DD)") @RequestParam String startDate,
//            @Parameter(description = "조회 종료일 (YYYY-MM-DD)") @RequestParam String endDate) {
//        // DTO에서 LocalDate로 직접 받도록 수정하거나, 여기서 파싱
//        TransactionSummaryRequestDto requestDto = new TransactionSummaryRequestDto();
//        requestDto.setStartDate(java.time.LocalDate.parse(startDate));
//        requestDto.setEndDate(java.time.LocalDate.parse(endDate));
//
//        List<TransactionSummaryItemDto> summaries = transactionService.getMyTransactionSummary(auth0Id, requestDto);
//        return ResponseEntity.ok(summaries);
//    }
}