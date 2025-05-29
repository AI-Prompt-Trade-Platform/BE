package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.dto.response.UserMypageResponse;
import org.example.prumpt_be.dto.response.PromptSummary;
import org.example.prumpt_be.repository.PromptPurchaseRepository;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PromptRepository promptRepository;
    private final PromptPurchaseRepository promptPurchaseRepository;

    public UserMypageResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<PromptSummary> sellingPrompts = promptRepository.findByOwnerId(userId)
                .stream().map(this::toPromptSummary).collect(Collectors.toList());

        List<PromptSummary> completedPurchases = promptPurchaseRepository.findByBuyerUserIdAndStatus(userId, "COMPLETED")
                .stream().map(p -> toPromptSummary(p.getPrompt())).collect(Collectors.toList());

        List<PromptSummary> ongoingPurchases = promptPurchaseRepository.findByBuyerUserIdAndStatus(userId, "IN_PROGRESS")
                .stream().map(p -> toPromptSummary(p.getPrompt())).collect(Collectors.toList());

        return UserMypageResponse.builder()
                .userId(user.getUserId())
                .profileName(user.getProfileName())
                .profileImgUrl(user.getProfileImgUrl())
                .bannerImgUrl(user.getBannerImgUrl())
                .introduction(user.getIntroduction())
                .userRole(user.getUserRole())
                .point(user.getPoint())
                .sellingPrompts(sellingPrompts)
                .completedPurchases(completedPurchases)
                .ongoingPurchases(ongoingPurchases)
                .build();
    }

    private PromptSummary toPromptSummary(Prompt prompt) {
        return PromptSummary.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .aiInspectionRate(prompt.getAiInspectionRate())
                .exampleContentUrl(prompt.getExampleContentUrl())
                .build();
    }
}