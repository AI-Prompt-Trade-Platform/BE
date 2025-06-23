package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.response.DailySalesDto; // DTO 임포트
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface PurchasesRepository extends JpaRepository<PromptPurchase, Integer> {

    // PromptTradeService에서 사용되므로 이 메소드가 존재하는지 확인해야 합니다.
    boolean existsByBuyerAndPrompt(Users buyer, Prompt prompt);

    /**
     * [수정됨]
     * - Object[] 대신 타입-안전한 DTO를 반환합니다.
     * - WHERE 절에서 Users 엔티티를 직접 비교합니다.
     */
    @Query("""
        SELECT new org.example.prumpt_be.dto.response.DailySalesDto(
            COUNT(p),
            SUM(p.prompt.price)
        )
        FROM PromptPurchase p
        WHERE p.prompt.ownerID = :seller
          AND p.purchasedAt >= :start
          AND p.purchasedAt <  :end
    """)
    DailySalesDto findDailySalesBySeller( // 메소드 이름도 더 명확하게 변경
                                          @Param("seller") Users seller,
                                          @Param("start")  LocalDateTime start,
                                          @Param("end")    LocalDateTime end
    );
}