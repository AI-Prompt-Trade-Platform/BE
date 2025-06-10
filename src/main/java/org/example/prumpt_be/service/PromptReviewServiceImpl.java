package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.ReviewDTO;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.request.PromptReviewRequestDto;
import org.example.prumpt_be.dto.entity.PromptReview;
import org.example.prumpt_be.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromptReviewServiceImpl implements PromptReviewService {

    private final PromptReviewsRepository reviewsRepository;
    private final PromptRepository promptRepository;
    private final UsersRepository usersRepository;
    private final PromptPurchaseRepository purchaseRepository;

    // 프롬프트 리뷰 등록
    @Override
    public void createReview(String auth0Id, PromptReviewRequestDto request) { //todo: 유저 ID 검증 메커니즘 필요 (완)
        if (usersRepository.findByAuth0Id(auth0Id).isPresent()) {
//            // 1. 리뷰 작성자 설정
//            review.setReviewer(
//                    userRepository.findById(auth0Id)
//                            .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + request.getUserId()))
//            );
//
//            // 2. 리뷰대상 프롬프트 ID로 조회하여 설정
//            review.setPromptID(
//                    promptRepository.findById(request.getPromptId())
//                            .orElseThrow(() -> new IllegalArgumentException("해당 프롬프트를 찾을 수 없습니다: " + request.getPromptId()))
//            );
//
//            // 3. 구매 내역 조회
//            PromptPurchase purchase = purchaseRepository
//                    .existsByBuyerAndPrompt(prompt, reviewer)
//                    .orElseThrow(() -> new IllegalArgumentException(
//                            "해당 프롬프트를 구매한 내역이 없습니다. promptId=" + request.getPromptId()));
//
//            review.setRate(request.getRate());
//            review.setReviewContent(request.getReviewContent());
//            review.setReviewedAt(LocalDateTime.now());
//
//            reviewsRepository.save(review);
            // 1) 사용자 조회
            Users reviewer = usersRepository.findByAuth0Id(auth0Id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + auth0Id));

            // 2) 프롬프트 조회
            Prompt prompt = promptRepository.findById(request.getPromptId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 프롬프트를 찾을 수 없습니다: " + request.getPromptId()));

            // 3) 구매 내역 조회
            PromptPurchase purchase = purchaseRepository
                    .findByBuyerAndPrompt(reviewer, prompt)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "해당 프롬프트를 구매한 내역이 없습니다. promptId=" + request.getPromptId()));

            // Todo: 리뷰 있으면 있다는 메세지 반환 로직 필요

            // 4) 리뷰 엔티티 생성 및 필드 세팅
            PromptReview review = new PromptReview();
            review.setReviewer(reviewer);
            review.setPromptID(prompt);          // 엔티티 필드명이 prompt라면 setPrompt(...)
            review.setPurchase(purchase);      // purchase_id NOT NULL
            review.setRate(request.getRate());
            review.setReviewContent(request.getReviewContent());
            review.setReviewedAt(LocalDateTime.now());

            // 5) 저장
            reviewsRepository.save(review);
        } else {
            throw new IllegalArgumentException("유효하지 않은 사용자 ID입니다: " + auth0Id);
        }

    }

    // 프롬프트 리뷰 조회
    @Override
    public List<ReviewDTO> getReviewsByPromptId(Long promptId) {
        return reviewsRepository.findAllByPromptIdAsDTO(promptId);
    }


    // 프롬프트 리뷰 수정
    @Override
    public void updateReview(String auth0id, Long reviewId, PromptReviewRequestDto request) {//todo: 유저 ID 검증 메커니즘 필요 (완)
        Integer userID = usersRepository.findByAuth0Id(auth0id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + auth0id))
                .getUserId();

        PromptReview review = reviewsRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다: " + reviewId));

        if(reviewsRepository.findByReviewIdAndReviewerUserId(reviewId, userID).isPresent()) {
            review.setRate(request.getRate());
            review.setReviewContent(request.getReviewContent());
            review.setReviewedAt(LocalDateTime.now());
            reviewsRepository.save(review);
        }
    }

    // 프롬프트 리뷰 삭제
    @Override
    public void deleteReview(String auth0Id, Long reviewId) { //todo: 유저 ID 검증 메커니즘 필요 (완)
        Integer userID = usersRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + auth0Id))
                .getUserId();
        if(reviewsRepository.findByReviewIdAndReviewerUserId(reviewId, userID).isPresent()) {
            reviewsRepository.deleteById(reviewId);
        }
        else {
            throw new IllegalArgumentException("해당 리뷰를 삭제할 권한이 없습니다: " + reviewId);
        } //todo: 상태별 메세지 여러개로 해두면 나쁘지않을듯
    }
}
