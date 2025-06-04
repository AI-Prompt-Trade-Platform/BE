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

    @GetMapping("/{promptId}")
    public ResponseEntity<List<PromptReview>> getReviews(@PathVariable Long promptId) {
        return ResponseEntity.ok(reviewService.getReviewsByPromptId(promptId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> updateReview(
            @PathVariable Long reviewId,
            @RequestBody PromptReviewRequestDTO request
    ) {
        reviewService.updateReview(reviewId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }


}
