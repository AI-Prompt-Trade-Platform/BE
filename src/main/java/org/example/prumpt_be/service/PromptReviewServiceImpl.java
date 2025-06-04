package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.request.PromptReviewRequestDTO;
import org.example.prumpt_be.entity.PromptReview;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.repository.PromptReviewRepository;
import org.example.prumpt_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptReviewServiceImpl implements PromptReviewService {

    private final PromptReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PromptRepository promptRepository;

    @Override
    public void createReview(PromptReviewRequestDTO request) {
        PromptReview review = new PromptReview();
        review.setUser(
                userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + request.getUserId()))
        );

        review.setPrompt(
                promptRepository.findById(request.getPromptId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 프롬프트를 찾을 수 없습니다: " + request.getPromptId()))
        );

        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    @Override
    public List<PromptReview> getReviewsByPromptId(Long promptId) {
        return reviewRepository.findByPromptPromptId(promptId);
    }

    @Override
    public void updateReview(Long reviewId, PromptReviewRequestDTO request) {
        PromptReview review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다: " + reviewId));
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }


}
