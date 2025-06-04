package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Purchases;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface PurchasesRepository extends JpaRepository<Purchases, Integer> {

     // 특정 기간(start ~ end) 동안
     // 특정 판매자의 판매 건수와 매출 합계를 조회
     // 반환: [ { sellerId, soldCount, totalRevenue }, … ]
    @Query(
            "SELECT COUNT(p), SUM(p.promptId.price) " +
                    "FROM Purchases p " +
                    "WHERE p.promptId.ownerID.userID = :userId " +
                    "  AND p.purchasedAt >= :start " +
                    "  AND p.purchasedAt <  :end"
    )
    Object[] findDailyProfitBySeller(
            @Param("userId") Integer userId,
            @Param("start")  LocalDateTime start,
            @Param("end")    LocalDateTime end
    );
}
