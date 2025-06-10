package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.entity.UserSalesSummary;
import org.example.prumpt_be.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PromptTradeService {

    private final PromptsRepository promptsRepository;
    private final PromptPurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final UserSalesSummaryRepository summaryRepository;

    @Transactional
    public void purchasePrompt(Integer promptId, String buyerId) {
        Prompt prompt = promptsRepository.findById(Long.valueOf(promptId))
                .orElseThrow(() -> new RuntimeException("프롬프트가 존재하지 않습니다"));

        Users buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("구매자 정보 없음"));

        if (Objects.equals(prompt.getOwnerID().getUserId(), buyerId)) {
            throw new RuntimeException("자기 자신의 프롬프트는 구매할 수 없습니다");
        }

        // 구매한 프롬프트 확인 todo: FE에서 구매버튼 비활성화 처리 필요
        if (purchaseRepository.existsByBuyerAndPrompt(buyer, prompt)) {
            throw new RuntimeException("이미 구매한 프롬프트입니다");
        }

        // 판매자 정보 조회
        Users seller = promptsRepository.findOwnerUserIdByPromptIdAndOwnerAuth0Id(prompt.getPromptId(), buyerId)
                .orElseThrow(() -> new RuntimeException("판매자 정보 없음"));

        int price = prompt.getPrice();
        if (buyer.getPoint() < price) {
            throw new RuntimeException("포인트가 부족합니다");
        }

        // 포인트 거래 todo: 보안 더 확실하게 처리 (단순 save()보다 DB 트랜잭션을 사용해서 동시성 문제 방지)
        buyer.setPoint(buyer.getPoint() - price);
        seller.setPoint(seller.getPoint() + price);
        userRepository.save(buyer);
        userRepository.save(seller);

        // 구매 이력 저장
        PromptPurchase purchase = new PromptPurchase();
        purchase.setPrompt(prompt);
        purchase.setBuyer(buyer);
        purchase.setPurchasedAt(LocalDateTime.now());
        purchaseRepository.save(purchase);

        // 판매 요약 업데이트
        UserSalesSummary summary = summaryRepository.findByUserIDAndSummaryDate(seller.getUserId(), LocalDate.now())
                .orElse(null);;

        if (summary == null) {
            // 새로운 요약 생성
            summary = new UserSalesSummary();
            summary.setUserID(seller);  // Users 객체
            summary.setSummaryDate(LocalDate.now());
            summary.setSoldCount(1);    // 첫 판매
            summary.setTotalRevenue(BigDecimal.valueOf(price));
        } else {
            // 기존 요약 업데이트
            summary.setSoldCount(summary.getSoldCount() + 1);
            summary.setTotalRevenue(summary.getTotalRevenue().add(BigDecimal.valueOf(price)));
        }
// lastUpdated는 @UpdateTimestamp로 자동 처리됨

        summaryRepository.save(summary);
    }
}
