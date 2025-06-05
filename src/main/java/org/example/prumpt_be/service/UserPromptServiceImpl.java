package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PurchasedPromptDto;
import org.example.prumpt_be.dto.response.SellingPromptDto;
import org.example.prumpt_be.repository.PromptPurchaseRepository;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.repository.UserRepository;
import org.example.prumpt_be.service.UserPromptService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal; // SellingPromptDto 에 필요하면 추가

/**
 * UserPromptService의 구현체입니다.
 * 사용자의 구매/판매 프롬프트 관련 로직을 실제로 수행합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserPromptServiceImpl implements UserPromptService {

    private final UserRepository userRepository;
    private final PromptRepository promptRepository;
    private final PromptPurchaseRepository promptPurchaseRepository;
    // private final PromptReviewRepository promptReviewRepository; // 리뷰 정보 포함 시 필요

    @Override
    public PageResponseDto<PurchasedPromptDto> getMyPurchasedPrompts(String auth0Id, Pageable pageable) {
        User currentUser = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found with auth0Id: " + auth0Id));

        Page<PromptPurchase> purchasesPage = promptPurchaseRepository.findByBuyerOrderByPurchasedAtDesc(currentUser, pageable);
        
        return new PageResponseDto<>(purchasesPage.map(this::convertToPurchasedPromptDto));
    }

    @Override
    public PageResponseDto<SellingPromptDto> getMySellingPrompts(String auth0Id, Pageable pageable) {
        User currentUser = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found with auth0Id: " + auth0Id));

        Page<Prompt> sellingPromptsPage = promptRepository.findByOwnerOrderByCreatedAtDesc(currentUser, pageable);
        
        return new PageResponseDto<>(sellingPromptsPage.map(this::convertToSellingPromptDto));
    }

    // --- Helper Methods ---
    private PurchasedPromptDto convertToPurchasedPromptDto(PromptPurchase purchase) {
        Prompt prompt = purchase.getPrompt();
        // Optional<PromptReview> reviewOpt = promptReviewRepository.findByPurchase_PurchaseId(purchase.getPurchaseId()); // 리뷰 정보 조회
        return PurchasedPromptDto.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .ownerProfileName(prompt.getOwner() != null ? prompt.getOwner().getProfileName() : "Unknown")
                .thumbnailImageUrl(prompt.getExampleContentUrl())
                .purchasedAt(purchase.getPurchasedAt())
                // .reviewId(reviewOpt.map(PromptReview::getReviewId).orElse(null))
                // .reviewRate(reviewOpt.map(PromptReview::getRate).orElse(null))
                .build();
    }

    private SellingPromptDto convertToSellingPromptDto(Prompt prompt) {
        // 판매 통계 (총 판매 건수, 수익)는 별도 집계 로직이 필요합니다.
        // UserSalesSummary 테이블을 활용하거나, PromptPurchaseRepository에서 count/sum 쿼리를 수행할 수 있습니다.
        // 여기서는 DTO 필드에 우선 null 또는 0으로 채워둡니다.
        return SellingPromptDto.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .aiInspectionRate(prompt.getAiInspectionRate())
                .createdAt(prompt.getCreatedAt())
                .updatedAt(prompt.getUpdatedAt())
                .totalSalesCount(0L)    // TODO: 실제 판매 건수 집계
                .totalRevenue(BigDecimal.ZERO) // TODO: 실제 판매 수익 집계
                .build();
    }
}