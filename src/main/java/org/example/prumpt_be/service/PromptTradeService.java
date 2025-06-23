package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.*;
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
    public void purchasePrompt(Integer promptId, String buyerAuth0Id) {
        // 1. 프롬프트 조회
        Prompt prompt = promptsRepository.findById(Long.valueOf(promptId))
                .orElseThrow(() -> new RuntimeException("프롬프트가 존재하지 않습니다. ID: " + promptId));

        // 2. 구매자 정보 조회
        Users buyer = userRepository.findByAuth0Id(buyerAuth0Id)
                .orElseThrow(() -> new RuntimeException("구매자 정보를 찾을 수 없습니다."));

        // 3. 판매자 정보는 prompt 객체에서 직접 가져옵니다. (DB 재조회 불필요)
        Users seller = prompt.getOwnerID();
        if (seller == null) {
            throw new RuntimeException("판매자 정보를 찾을 수 없습니다.");
        }

        // 4. 자기 자신의 프롬프트 구매 시도 방지 (auth0Id로 비교)
        if (Objects.equals(seller.getAuth0Id(), buyerAuth0Id)) {
            throw new RuntimeException("자기 자신의 프롬프트는 구매할 수 없습니다.");
        }

        // 5. 이미 구매한 프롬프트인지 확인
        if (purchaseRepository.existsByBuyerAndPrompt(buyer, prompt)) {
            throw new RuntimeException("이미 구매한 프롬프트입니다.");
        }

        // 6. 포인트 확인 및 차감/증가
        int price = prompt.getPrice();
        if (buyer.getPoint() < price) {
            throw new RuntimeException("포인트가 부족합니다.");
        }

        buyer.setPoint(buyer.getPoint() - price);
        seller.setPoint(seller.getPoint() + price);
        // @Transactional에 의해 메소드 종료 시 자동으로 save 처리됩니다.
        // 명시적으로 save를 호출할 필요는 없습니다.

        // 7. 구매 이력 저장
        PromptPurchase purchase = PromptPurchase.builder()
                .prompt(prompt)
                .buyer(buyer)
                .purchasedAt(LocalDateTime.now())
                .build();
        purchaseRepository.save(purchase);

        // 8. 판매 요약 업데이트 (수정된 부분)
        LocalDate today = LocalDate.now();
        UserSalesSummary summary = summaryRepository.findById_UserIDAndId_SummaryDate(seller, today)
                .orElseGet(() -> {
                    // 8-1. 복합 키 객체를 먼저 생성합니다.
                    UserSalesSummaryId newId = new UserSalesSummaryId(seller, today);

                    // 8-2. 생성된 ID를 사용하여 엔티티를 빌드합니다.
                    return UserSalesSummary.builder()
                            .id(newId) // userID, summaryDate 대신 id 객체를 통째로 전달
                            .soldCount(0)
                            .totalRevenue(BigDecimal.ZERO)
                            .build();
                });

        summary.setSoldCount(summary.getSoldCount() + 1);
        summary.setTotalRevenue(summary.getTotalRevenue().add(BigDecimal.valueOf(price)));
        summary.setLastUpdated(LocalDateTime.now()); // lastUpdated 필드 추가
        summaryRepository.save(summary);
    }
}
