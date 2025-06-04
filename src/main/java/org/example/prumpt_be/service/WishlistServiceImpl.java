package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.dto.entity.UserWishlist;
import org.example.prumpt_be.dto.response.PageResponseDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.repository.UserRepository;
import org.example.prumpt_be.repository.WishlistRepository; // UserWishlistRepository의 이름이 WishlistRepository라고 가정
import org.example.prumpt_be.service.WishlistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * WishlistService 인터페이스의 구현체입니다.
 * 위시리스트 관련 실제 비즈니스 로직을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final UserRepository userRepository;
    private final PromptRepository promptRepository;
    private final WishlistRepository wishlistRepository; // JpaRepository<UserWishlist, Long> 타입

    @Override
    @Transactional
    public void addPromptToWishlist(String auth0Id, Long promptId) {
        User user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id)); // TODO: 맞춤형 예외 처리
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("프롬프트를 찾을 수 없습니다. ID: " + promptId)); // TODO: 맞춤형 예외 처리

        // 이미 위시리스트에 있는지 확인
        Optional<UserWishlist> existingWish = wishlistRepository.findByUserAndPrompt(user, prompt);
        if (existingWish.isPresent()) {
            // 이미 존재하면 별도의 작업을 하지 않거나, 예외를 발생시킬 수 있습니다.
            // 여기서는 멱등성을 위해 별도 작업 없이 반환합니다.
            return;
            // throw new RuntimeException("이미 위시리스트에 추가된 프롬프트입니다.");
        }

        UserWishlist newWish = UserWishlist.builder()
                .user(user)
                .prompt(prompt)
                // addedAt은 @CreationTimestamp에 의해 자동 설정됩니다.
                .build();
        wishlistRepository.save(newWish);
    }

    @Override
    @Transactional
    public void removePromptFromWishlist(String auth0Id, Long promptId) {
        User user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id));
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("프롬프트를 찾을 수 없습니다. ID: " + promptId));

        UserWishlist wish = wishlistRepository.findByUserAndPrompt(user, prompt)
                .orElseThrow(() -> new RuntimeException("위시리스트에서 해당 프롬프트를 찾을 수 없습니다."));

        wishlistRepository.delete(wish);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDto<PromptSummaryDto> getUserWishlist(String auth0Id, Pageable pageable) {
        User user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id));

        // WishlistRepository에 findByUserOrderByAddedAtDesc 메소드가 있어야 합니다.
        // 이 메소드는 UserWishlist 엔티티의 Page를 반환합니다.
        Page<UserWishlist> wishlistPage = wishlistRepository.findByUserOrderByAddedAtDesc(user, pageable);
        
        // UserWishlist 페이지를 PromptSummaryDto 페이지로 변환합니다.
        Page<PromptSummaryDto> promptSummaryDtoPage = wishlistPage.map(this::convertToPromptSummaryDtoFromWishlist);
        
        return new PageResponseDto<>(promptSummaryDtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isPromptInWishlist(String auth0Id, Long promptId) {
        User user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id));
        // Prompt 존재 여부도 확인하는 것이 좋습니다.
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("프롬프트를 찾을 수 없습니다. ID: " + promptId));
                
        return wishlistRepository.findByUserAndPrompt(user, prompt).isPresent();
        // 또는 WishlistRepository에 existsByUserAndPrompt(User user, Prompt prompt) 메소드를 만들어서 사용
        // return wishlistRepository.existsByUserAndPrompt(user, prompt); 
    }

    // --- Helper Methods ---
    // UserWishlist 엔티티에서 Prompt 정보를 가져와 PromptSummaryDto로 변환
    private PromptSummaryDto convertToPromptSummaryDtoFromWishlist(UserWishlist userWishlist) {
        if (userWishlist == null || userWishlist.getPrompt() == null) {
            return null;
        }
        Prompt prompt = userWishlist.getPrompt();
        String ownerName = (prompt.getOwner() != null && prompt.getOwner().getProfileName() != null)
                ? prompt.getOwner().getProfileName()
                : "Unknown";

        return PromptSummaryDto.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .ownerProfileName(ownerName)
                .thumbnailImageUrl(prompt.getExampleContentUrl())
                .aiInspectionRate(prompt.getAiInspectionRate())
                // 위시리스트 DTO에는 프롬프트의 생성일보다는 위시리스트에 추가된 날짜가 더 의미있을 수 있습니다.
                // 필요하다면 PromptSummaryDto를 확장한 WishlistPromptDto를 만들거나,
                // PromptSummaryDto에 addedAt 필드를 추가하고 userWishlist.getAddedAt() 값을 설정할 수 있습니다.
                // 여기서는 일단 프롬프트의 createdAt을 사용합니다.
                .createdAt(prompt.getCreatedAt()) 
                .build();
    }
}