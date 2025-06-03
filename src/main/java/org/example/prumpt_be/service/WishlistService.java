package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.response.WishlistPromptResponse;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.dto.entity.UserWishlist;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.repository.UserRepository;
import org.example.prumpt_be.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final PromptRepository promptRepository;
    private final UserRepository userRepository;

    // 유저의 위시리스트 반환
    public List<WishlistPromptResponse> getUserWishlist(Long userId) {
        List<UserWishlist> wishlistItems = wishlistRepository.findByUserId(userId);

        return wishlistItems.stream()
            .map(item -> {
                Prompt prompt = promptRepository.findById(item.getPromptId())
                    .orElseThrow(() -> new IllegalArgumentException("프롬프트 없음"));
                User owner = userRepository.findById(prompt.getOwnerId())
                    .orElseThrow(() -> new IllegalArgumentException("소유자 없음"));
                return WishlistPromptResponse.builder()
                    .promptId(prompt.getPromptId())
                    .promptName(prompt.getPromptName())
                    .price(prompt.getPrice())
                    .exampleContentUrl(prompt.getExampleContentUrl())
                    .ownerName(owner.getProfileName())
                    .build();
            })
            .collect(Collectors.toList());
    }

    // 위시리스트에 프롬프트 추가 todo: 추가/삭제 토글방식으로 수정 필요
    public void addToWishlist(Long userId, Long promptId) {
        if (!wishlistRepository.existsByUserIdAndPromptId(userId, promptId)) {
            wishlistRepository.save(UserWishlist.builder()
                .userId(userId)
                .promptId(promptId)
                .addedAt(LocalDateTime.now())
                .build());
        }
    }

    // 위시리스트에서 프롬프트 제거
    public void removeFromWishlist(Long userId, Long promptId) {
        Optional<UserWishlist> wishlist = wishlistRepository.findByUserIdAndPromptId(userId, promptId);
        wishlist.ifPresent(wishlistRepository::delete);
    }

}