package org.example.prumpt_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.ReviewDTO;
import org.example.prumpt_be.dto.request.PromptReviewRequestDto;
import org.example.prumpt_be.service.PromptReviewServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class PromptReviewController {

    private final PromptReviewServiceImpl promptReviewServiceImpl;

    // 프롬프트 리뷰 등록
    @Operation(summary = "리뷰 작성 API", description = "토큰, 리뷰데이터를 받아 리뷰 생성.")
    @PostMapping
    public ResponseEntity<Void> createReview(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody PromptReviewRequestDto request) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        promptReviewServiceImpl.createReview(userAuth0Id, request);
        return ResponseEntity.ok().build();
    }

    // 프롬프트 리뷰 조회
    @Operation(summary = "리뷰 조회 API", description = "프롬프트ID 를 입력받아 프롬프트의 리뷰 조회.")
    @GetMapping("/{promptId}")
    public ResponseEntity<List<ReviewDTO>> getReviews(
            @PathVariable Long promptId) {
        return ResponseEntity.ok(promptReviewServiceImpl.getReviewsByPromptId(promptId));
    }

    // 프롬프트 리뷰 수정
    @Operation(summary = "리뷰 수정 API", description = "리뷰ID 를 입력받아 리뷰 수정.")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reviewId,
            @RequestBody PromptReviewRequestDto request
    ) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        promptReviewServiceImpl.updateReview(userAuth0Id, reviewId, request);
        return ResponseEntity.ok().build();
    }

    // 프롬프트 리뷰 삭제
    @Operation(summary = "리뷰 삭제 API", description = "auth0Id와 리뷰ID 를 입력받아 프롬프트의 리뷰 삭제.")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reviewId) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        promptReviewServiceImpl.deleteReview(userAuth0Id, reviewId);
        return ResponseEntity.noContent().build();
    }


}
