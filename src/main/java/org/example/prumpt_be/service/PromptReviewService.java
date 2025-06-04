package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.request.PromptReviewRequestDTO;
import org.example.prumpt_be.entity.PromptReview;

import java.util.List;

public interface PromptReviewService {
    void createReview(PromptReviewRequestDTO request);
    List<PromptReview> getReviewsByPromptId(Long promptId);
    void updateReview(Long reviewId, PromptReviewRequestDTO request); //todo: 유저 ID 검증 메커니즘 필요
    void deleteReview(Long reviewId); //todo: 유저 ID 검증 메커니즘 필요

}
