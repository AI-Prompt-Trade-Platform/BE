package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.ReviewDTO;
import org.example.prumpt_be.dto.request.PromptReviewRequestDto;

import java.util.List;

public interface PromptReviewService {
    default void createReview(String userId, PromptReviewRequestDto request){}
    List<ReviewDTO> getReviewsByPromptId(Long promptId);
    void updateReview(String userId, Long reviewId, PromptReviewRequestDto request); //todo: 유저 ID 검증 메커니즘 필요
    void deleteReview(String userId, Long reviewId); //todo: 유저 ID 검증 메커니즘 필요

}
