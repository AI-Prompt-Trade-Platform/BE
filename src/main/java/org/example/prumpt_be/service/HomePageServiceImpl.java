package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.PromptReview;
import org.example.prumpt_be.dto.entity.Tag;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.example.prumpt_be.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // StringUtils 임포트

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * HomePageService의 구현체입니다.
 * 홈 화면 및 프롬프트 목록 관련 데이터 조회 로직을 실제로 수행합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomePageServiceImpl implements HomePageService {

    private final PromptRepository promptRepository;
    private final PromptClassificationRepository promptClassificationRepository;
    private final PromptReviewsRepository promptReviewsRepository;
    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;


    @Override
    public PageResponseDto<PromptSummaryDto> getPopularPrompts(Pageable pageable) {
        Page<Prompt> popularPromptsPage = promptRepository.findAll(pageable);
        return new PageResponseDto<>(popularPromptsPage.map(this::convertToPromptSummaryDto));
    }

    @Override
    public PageResponseDto<PromptSummaryDto> getRecentPrompts(Pageable pageable) {
        Page<Prompt> recentPromptsPage = promptRepository.findAll(pageable);
        return new PageResponseDto<>(recentPromptsPage.map(this::convertToPromptSummaryDto));
    }

    @Override
    public PageResponseDto<PromptSummaryDto> searchPrompts(String keyword, Pageable pageable) {
        // StringUtils.hasText를 사용하여 null, 빈 문자열, 공백만 있는 문자열을 모두 체크
        if (!StringUtils.hasText(keyword)) {
            Page<Prompt> emptyKeywordPromptsPage = promptRepository.findAll(pageable);
            return new PageResponseDto<>(emptyKeywordPromptsPage.map(this::convertToPromptSummaryDto));
        }
        Page<Prompt> searchedPromptsPage = promptRepository.findByKeyword(keyword.trim(), pageable);
        return new PageResponseDto<>(searchedPromptsPage.map(this::convertToPromptSummaryDto));
    }

    @Override
    public PageResponseDto<PromptSummaryDto> filterPromptsByCategories(
            String modelCategorySlug,
            String typeCategorySlug,
            Pageable pageable) {

        // 슬러그 값이 비어있거나 공백만 있으면 null로 처리하여 Repository에서 조건 무시하도록 함
        String finalModelSlug = StringUtils.hasText(modelCategorySlug) ? modelCategorySlug.trim() : null;
        String finalTypeSlug = StringUtils.hasText(typeCategorySlug) ? typeCategorySlug.trim() : null;

        Page<Prompt> filteredPromptsPage = promptRepository.findPromptsByCategories(
                finalModelSlug,
                finalTypeSlug,
                pageable
        );
        return new PageResponseDto<>(filteredPromptsPage.map(this::convertToPromptSummaryDto));
    }

    // --- Helper Methods ---
    private PromptSummaryDto convertToPromptSummaryDto(Prompt prompt) {
        if (prompt == null) {
            return null;
        }
        return getPromptSummaryDto(prompt);
    }

    static PromptSummaryDto getPromptSummaryDto(Prompt prompt) {
        String ownerName = (prompt.getOwnerID() != null && prompt.getOwnerID().getProfileName() != null)
                ? prompt.getOwnerID().getProfileName()
                : "Unknown";

        // 1. typeCategory가 null일 경우를 대비하여 안전하게 조회
        String typeCategoryName = "N/A"; // 기본값 설정
        if (prompt.getClassifications() != null && prompt.getClassifications().getTypeCategory() != null) {
            typeCategoryName = prompt.getClassifications().getTypeCategory().getTypeName();
        }

        // 2. reviews 컬렉션이 null이 아닐 경우에만 평균 평점 계산
        double averageRate = 0.0;
        if (prompt.getReviews() != null && !prompt.getReviews().isEmpty()) {
            averageRate = prompt.getReviews().stream()
                    .mapToDouble(PromptReview::getRate)
                    .average()
                    .orElse(0.0);
        }

        // 3. purchases 컬렉션이 null이 아닐 경우에만 판매 수 계산
        int salesCount = 0;
        if (prompt.getPurchases() != null) {
            salesCount = prompt.getPurchases().size();
        }

        // 4. tags 컬렉션이 null이 아닐 경우에만 해시태그 목록 생성
        List<String> hashTags = new ArrayList<>();
        if (prompt.getTags() != null) {
            hashTags = prompt.getTags().stream().map(Tag::getName).toList();
        }

        return PromptSummaryDto.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .ownerProfileName(ownerName)
                .thumbnailImageUrl(prompt.getExampleContentUrl())
                .aiInspectionRate(prompt.getAiInspectionRate())
                .createdAt(prompt.getCreatedAt())
                .description(prompt.getPromptName())
                .typeCategory(typeCategoryName) // 안전하게 조회한 값 사용
                .rate(averageRate)             // 안전하게 계산한 값 사용
                .salesCount(salesCount)        // 안전하게 계산한 값 사용
                .hashTags(hashTags)            // 안전하게 생성한 리스트 사용
                .build();
    }

    public PromptDetailDTO getPromptDetailDto(String auth0Id , Prompt prompt) {
        String ownerName = (prompt.getOwnerID() != null && prompt.getOwnerID().getProfileName() != null)
                ? prompt.getOwnerID().getProfileName()
                : "Unknown";

        boolean isBookmarked = false;
        // auth0Id가 존재할 경우 (로그인한 사용자일 경우) 위시리스트 확인
        if (StringUtils.hasText(auth0Id)) {
            Optional<Users> userOptional = userRepository.findByAuth0Id(auth0Id);
            if (userOptional.isPresent()) {
                Users currentUser = userOptional.get();
                // 현재 사용자와 프롬프트로 위시리스트에 있는지 확인
                isBookmarked = wishlistRepository.existsByUserAndPrompt(currentUser, prompt);
            }
        }

        return PromptDetailDTO.builder()
                .id(prompt.getPromptId())
                .title(prompt.getPromptName())
                .description(prompt.getDescription())
                .content(prompt.getPromptContent())
                .price(prompt.getPrice() != null ? prompt.getPrice() : 0)
                .ownerProfileName(ownerName)
                .categories(promptClassificationRepository.findDtoByPromptId(prompt.getPromptId()))
                .thumbnailImageUrl(prompt.getExampleContentUrl())
                .aiInspectionRate(prompt.getAiInspectionRate())
                .createdAt(prompt.getCreatedAt())
                .reviews(promptReviewsRepository.findAllByPromptIdAsDTO(prompt.getPromptId()))
                .averageRating(promptReviewsRepository.findAverageRateByPromptId(prompt.getPromptId()).orElse(0.0))
                .isBookmarked(isBookmarked)
                .auth0id(prompt.getOwnerID().getAuth0Id())
                .build();
    }
}