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

    private final PromptRepository promptRepository;
    private final PromptPurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final UserSalesSummaryRepository summaryRepository;

    @Transactional
    public void purchasePrompt(Integer promptId, Integer buyerId) {
        Prompt prompt = promptRepository.findById(Long.valueOf(promptId))
                .orElseThrow(() -> new RuntimeException("프롬프트가 존재하지 않습니다"));

        Users buyer = userRepository.findById(Long.valueOf(buyerId))
                .orElseThrow(() -> new RuntimeException("구매자 정보 없음"));

        if (Objects.equals(prompt.getOwnerID().getUserID(), buyerId)) {
            throw new RuntimeException("자기 자신의 프롬프트는 구매할 수 없습니다");
        }

        // 구매한 프롬프트 확인 todo: FE에서 구매버튼 비활성화 처리 필요
        if (purchaseRepository.existsByBuyerIdAndPromptId(buyerId, promptId)) {
            throw new RuntimeException("이미 구매한 프롬프트입니다");
        }

        Users seller = userRepository.findById(Long.valueOf(prompt.getOwnerID().getUserID()))
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
        purchase.setPromptId(promptId);
        purchase.setBuyerId(buyerId);
        purchase.setPurchasedAt(LocalDateTime.now());
        purchaseRepository.save(purchase);

        // 판매 요약 업데이트
        UserSalesSummary summary = summaryRepository.findByUserIdAndSummaryDate(seller.getUserID(), LocalDate.now())
                .orElseGet(() -> new UserSalesSummary(seller.getUserID(), LocalDate.now()));

        summary.setSoldCount(summary.getSoldCount() + 1);
        summary.setTotalRevenue(summary.getTotalRevenue().add(BigDecimal.valueOf(price)));
        summary.setLastUpdated(LocalDateTime.now());

        summaryRepository.save(summary);
    }
}
