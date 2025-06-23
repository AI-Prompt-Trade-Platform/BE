package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.dto.ReviewDTO;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.response.ClassificationDTO;
import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PurchasedPromptDto;
import org.example.prumpt_be.dto.response.SellingPromptDto;
import org.example.prumpt_be.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal; // SellingPromptDto 에 필요하면 추가
import java.util.List;
import java.util.stream.Collectors;

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
    private final PromptReviewsRepository promptReviewsRepository; // 리뷰 정보 포함 시 필요
    private final PromptClassificationRepository promptClassificationRepository; // 카테고리 정보 포함 시 필요


    @Override
    public PageResponseDto<PurchasedPromptDto> getMyPurchasedPrompts(String auth0Id, Pageable pageable) {
        Users currentUser = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found with auth0Id: " + auth0Id));

        Page<PromptPurchase> purchasesPage = promptPurchaseRepository.findByBuyerOrderByPurchasedAtDesc(currentUser, pageable);
        
        return new PageResponseDto<>(purchasesPage.map(this::convertToPurchasedPromptDto));
    }

    @Override
    public PageResponseDto<PromptDetailDTO> getMySellingPrompts(String auth0Id, Pageable pageable) {
        // 1. 사용자 조회
        Users currentUser = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found with auth0Id: " + auth0Id));

        // 2. 해당 사용자가 소유한(판매 중인) 프롬프트를 페이지네이션하여 조회
        Page<Prompt> sellingPromptsPage = promptRepository.findByOwnerIDOrderByCreatedAtDesc(currentUser, pageable);

        // 3. Page<Prompt>를 Page<PromptDetailDTO>로 변환
        Page<PromptDetailDTO> promptDetailDTOPage = sellingPromptsPage.map(prompt -> convertToPromptDetailDTO(prompt, currentUser));

        // 4. PageResponseDto로 감싸서 반환
        return new PageResponseDto<>(promptDetailDTOPage);
    }

    // --- Helper Methods ---
    private PurchasedPromptDto convertToPurchasedPromptDto(PromptPurchase purchase) {
        Prompt prompt = purchase.getPrompt();
        // Optional<PromptReview> reviewOpt = promptReviewRepository.findByPurchase_PurchaseId(purchase.getPurchaseId()); // 리뷰 정보 조회
        return PurchasedPromptDto.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .ownerProfileName(prompt.getOwnerID() != null ? prompt.getOwnerID().getProfileName() : "Unknown")
                .thumbnailImageUrl(prompt.getExampleContentUrl())
                .purchasedAt(purchase.getPurchasedAt())
                .aiInspectionRate(prompt.getAiInspectionRate())
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

    /**
     * 현재 사용자가 판매 중인 프롬프트 목록을 상세 정보(PromptDetailDTO)로 조회합니다.
     *
     * @param auth0Id 현재 사용자의 Auth0 ID
     * @return 판매 중인 프롬프트의 상세 정보 리스트
     */
    public List<PromptDetailDTO> getMySellingPromptsAsDetail(String auth0Id) {
        // 1. 사용자 조회
        Users user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("User not found with auth0Id: " + auth0Id));

        // 2. 해당 사용자가 소유한(판매 중인) 모든 프롬프트 조회
        List<Prompt> prompts = promptRepository.findByOwnerID(user);

        // 3. 각 Prompt 엔티티를 PromptDetailDTO로 변환
        return prompts.stream()
                .map(prompt -> convertToPromptDetailDTO(prompt, user)) // DTO 변환 헬퍼 메소드 호출
                .collect(Collectors.toList());
    }

    /**
     * Prompt 엔티티를 PromptDetailDTO로 변환하는 헬퍼 메소드입니다.
     *
     * @param prompt 변환할 Prompt 엔티티
     * @param user   현재 사용자(판매자)
     * @return 변환된 PromptDetailDTO
     */
    private PromptDetailDTO convertToPromptDetailDTO(Prompt prompt, Users user) {
        // 프롬프트의 평균 별점과 리뷰 목록을 조회합니다.
        double averageRating = promptReviewsRepository.findAverageRateByPromptId(prompt.getPromptId()).orElse(0.0);
        List<ReviewDTO> reviews = promptReviewsRepository.findAllByPromptIdAsDTO(prompt.getPromptId());
        ClassificationDTO categories = promptClassificationRepository.findDtoByPromptId(prompt.getPromptId());
        String ownerName = (prompt.getOwnerID() != null && prompt.getOwnerID().getProfileName() != null)
                ? prompt.getOwnerID().getProfileName()
                : "Unknown";

        // PromptDetailDTO를 빌드합니다.
        return PromptDetailDTO.builder()
                .id(prompt.getPromptId())
                .title(prompt.getPromptName())
                .description(prompt.getDescription())
                .content(prompt.getPromptContent())
                .price(prompt.getPrice() != null ? prompt.getPrice() : 0)
                .ownerProfileName(ownerName)
                // 판매자 본인의 프롬프트이므로 isBookmarked와 userPurchased는 false로 처리합니다.
                .isBookmarked(false)
                .userPurchased(false)
                .averageRating(averageRating)
                .reviews(reviews)
                .categories(categories)
                .thumbnailImageUrl(prompt.getExampleContentUrl())
                .aiInspectionRate(prompt.getAiInspectionRate())
                .createdAt(prompt.getCreatedAt())
                .auth0id(prompt.getOwnerID().getAuth0Id())
                .build();
    }
}