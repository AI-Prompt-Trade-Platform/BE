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

    // 커스텀 예외 클래스
    public static class SelfReviewAttemptException extends RuntimeException {
        public SelfReviewAttemptException(String message) {
            super(message);
        }
    }

    // 프롬프트 리뷰 등록
    @Override
    public void createReview(String auth0Id, PromptReviewRequestDto request) {
        // 1) 사용자 및 프롬프트 조회
        Users reviewer = usersRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + auth0Id));

        Prompt prompt = promptRepository.findById(request.getPromptId())
                .orElseThrow(() -> new IllegalArgumentException("해당 프롬프트를 찾을 수 없습니다: " + request.getPromptId()));

        // 2) 리뷰 생성 전 유효성 검증 로직 실행
        validateReviewCreation(reviewer, prompt);

        // 3) 구매 내역 조회 (검증 후)
        PromptPurchase purchase = purchaseRepository
                .findByBuyerAndPrompt(reviewer, prompt)
                .orElseThrow(() -> new IllegalStateException( // 구매내역이 없는건 서버 로직상 문제일 수 있으므로 IllegalStateException 사용 가능
                        "해당 프롬프트를 구매한 내역이 없습니다. promptId=" + request.getPromptId()));

        // 4) 리뷰 엔티티 생성 및 저장
        PromptReview review = PromptReview.builder()
                .reviewer(reviewer)
                .promptID(prompt)
                .purchase(purchase)
                .rate(request.getRate())
                .reviewContent(request.getReviewContent())
                .reviewedAt(LocalDateTime.now())
                .build();

        reviewsRepository.save(review);
    }

    /**
     * 리뷰 생성에 대한 유효성 검증을 수행하는 private 메소드
     * @param reviewer 리뷰 작성자
     * @param prompt 리뷰 대상 프롬프트
     */
    private void validateReviewCreation(Users reviewer, Prompt prompt) {
        // 검증 1: 본인의 프롬프트인지 확인
        if (prompt.getOwnerID().getUserId().equals(reviewer.getUserId())) {
            // 커스텀 예외를 사용하여 더 명확하게 표현
            throw new SelfReviewAttemptException("본인의 프롬프트에는 리뷰를 작성할 수 없습니다.");
        }

        // 검증 2: 이미 리뷰를 작성했는지 확인 (TODO 해결)
        if (reviewsRepository.existsByReviewerAndPromptID(reviewer, prompt)) {
            throw new IllegalStateException("이미 해당 프롬프트에 대한 리뷰를 작성했습니다.");
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
