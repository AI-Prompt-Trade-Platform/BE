package org.example.prumpt_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.request.PromptReviewRequestDTO;
import org.example.prumpt_be.dto.entity.PromptReview;
import org.example.prumpt_be.service.PromptReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class PromptReviewController {

    private final PromptReviewService reviewService;

    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody PromptReviewRequestDTO request) {
        reviewService.createReview(request);
        return ResponseEntity.ok().build();
    }

    // 프롬프트 리뷰 조회
    @GetMapping("/{promptId}")
    public ResponseEntity<List<PromptReview>> getReviews(@PathVariable Long promptId) {
        return ResponseEntity.ok(reviewService.getReviewsByPromptId(promptId));
    }

    // 프롬프트 리뷰 수정
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview( //todo: 유저 ID 검증 메커니즘 필요
            @PathVariable Long reviewId,
            @RequestBody PromptReviewRequestDTO request
    ) {
        reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok().build();
    }

    // 프롬프트 리뷰 삭제
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) { //todo: 유저 ID 검증 메커니즘 필요
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }


}
