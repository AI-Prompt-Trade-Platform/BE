package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.example.prumpt_be.service.HomePageService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * 홈 화면 및 프롬프트 목록 조회 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 * 인기 프롬프트, 최신 프롬프트, 검색, 카테고리 필터링 기능을 담당합니다.
 */
@RestController
@RequestMapping("/api") // 기본 경로를 /api 로 변경하여 /api/home 과 /api/prompts 를 구분
@RequiredArgsConstructor
@Tag(name = "Prompts Listing / Home", description = "프롬프트 목록 조회 (홈, 필터링, 검색) 관련 API")
public class HomePageController { // 클래스명을 PromptListingController 등으로 변경 고려 가능

    private final HomePageService homePageService;

    @Operation(summary = "인기 프롬프트 목록 조회", description = "페이지네이션된 인기 프롬프트 목록을 조회합니다.")
    @GetMapping("/home/prompts/popular") // 기존 경로 유지
    public ResponseEntity<PageResponseDto<PromptSummaryDto>> getPopularPrompts(
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=price,DESC")
            @PageableDefault(size = 10, sort = "price", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<PromptSummaryDto> popularPrompts = homePageService.getPopularPrompts(pageable);
        return ResponseEntity.ok(popularPrompts);
    }

    @Operation(summary = "최근 프롬프트 목록 조회", description = "페이지네이션된 최근 등록 프롬프트 목록을 조회합니다.")
    @GetMapping("/home/prompts/recent") // 기존 경로 유지
    public ResponseEntity<PageResponseDto<PromptSummaryDto>> getRecentPrompts(
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=createdAt,DESC")
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<PromptSummaryDto> recentPrompts = homePageService.getRecentPrompts(pageable);
        return ResponseEntity.ok(recentPrompts);
    }

    @Operation(summary = "프롬프트 키워드 검색", description = "키워드를 포함하는 프롬프트 목록을 검색하여 페이지네이션된 결과로 반환합니다.")
    @GetMapping("/home/prompts/search") // 기존 경로 유지
    public ResponseEntity<PageResponseDto<PromptSummaryDto>> searchPrompts(
            @Parameter(description = "검색할 키워드") @RequestParam(required = false) String keyword,
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=createdAt,DESC")
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PageResponseDto<PromptSummaryDto> searchedPrompts = homePageService.searchPrompts(keyword, pageable);
        return ResponseEntity.ok(searchedPrompts);
    }

    @Operation(summary = "프롬프트 카테고리 필터링", description = "모델 카테고리 및/또는 타입 카테고리 슬러그로 프롬프트를 필터링합니다.")
    @GetMapping("/prompts/filter") // 새로운 필터링 엔드포인트
    public ResponseEntity<PageResponseDto<PromptSummaryDto>> filterPromptsByCategories(
            @Parameter(description = "모델 카테고리 슬러그 (예: 'chatgpt', 'midjourney')")
            @RequestParam(required = false) String modelCategorySlug,
            @Parameter(description = "타입 카테고리 슬러그 (예: 'image-generation', 'conversational-ai')")
            @RequestParam(required = false) String typeCategorySlug,
            @Parameter(description = "페이지 정보. 예: page=0&size=10&sort=createdAt,DESC")
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        PageResponseDto<PromptSummaryDto> filteredPrompts = homePageService.filterPromptsByCategories(
                modelCategorySlug,
                typeCategorySlug,
                pageable
        );
        return ResponseEntity.ok(filteredPrompts);
    }
}