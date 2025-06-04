package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.request.PromptReviewRequestDTO;
import org.example.prumpt_be.dto.entity.PromptReview;

import java.util.List;

public interface PromptReviewService {
    void createReview(PromptReviewRequestDTO request);
    List<PromptReview> getReviewsByPromptId(Long promptId);
    void updateReview(Long reviewId, PromptReviewRequestDTO request);
    void deleteReview(Long reviewId);

}
