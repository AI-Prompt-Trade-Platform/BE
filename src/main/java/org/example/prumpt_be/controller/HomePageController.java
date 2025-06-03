package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.example.prumpt_be.service.HomePageService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // Sort 인터페이스를 임포트해야 합니다.
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 홈 화면 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 * 인기 프롬프트, 최신 프롬프트 조회 및 프롬프트 검색 기능을 담당합니다.
 */
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Tag(name = "Home", description = "홈 화면 관련 API")
public class HomePageController {

    private final HomePageService homePageService;

    @Operation(summary = "인기 프롬프트 목록 조회", description = "페이지네이션된 인기 프롬프트 목록을 조회합니다.")
    @GetMapping("/prompts/popular")
    public ResponseEntity<PageResponseDto<PromptSummaryDto>> getPopularPrompts(
            @Parameter(description = "페이지 번호 (0부터 시작), 페이지 크기, 정렬 방식 등을 정의. 예: page=0&size=10&sort=price,DESC")
            // 인기 프롬프트의 기본 정렬 기준을 'price' 필드의 내림차순(DESC)으로 설정합니다.
            @PageableDefault(size = 10, sort = "price", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<PromptSummaryDto> popularPrompts = homePageService.getPopularPrompts(pageable);
        return ResponseEntity.ok(popularPrompts);
    }

    @Operation(summary = "최근 프롬프트 목록 조회", description = "페이지네이션된 최근 등록 프롬프트 목록을 조회합니다.")
    @GetMapping("/prompts/recent")
    public ResponseEntity<PageResponseDto<PromptSummaryDto>> getRecentPrompts(
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=createdAt,DESC")
            // 최근 프롬프트의 기본 정렬 기준을 'createdAt' 필드의 내림차순(DESC)으로 설정합니다.
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<PromptSummaryDto> recentPrompts = homePageService.getRecentPrompts(pageable);
        return ResponseEntity.ok(recentPrompts);
    }

    @Operation(summary = "프롬프트 검색", description = "키워드를 포함하는 프롬프트 목록을 검색하여 페이지네이션된 결과로 반환합니다.")
    @GetMapping("/prompts/search")
    public ResponseEntity<PageResponseDto<PromptSummaryDto>> searchPrompts(
            @Parameter(description = "검색할 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=createdAt,DESC")
            // 검색 결과의 기본 정렬 기준을 'createdAt' 필드의 내림차순(DESC)으로 설정합니다.
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<PromptSummaryDto> searchedPrompts = homePageService.searchPrompts(keyword, pageable);
        return ResponseEntity.ok(searchedPrompts);
    }
}