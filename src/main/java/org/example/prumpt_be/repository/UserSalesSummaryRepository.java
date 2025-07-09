package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.UserSalesSummary;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.response.EachDaysProfitDto;
import org.example.prumpt_be.dto.entity.UserSalesSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSalesSummaryRepository
        extends JpaRepository<UserSalesSummary, UserSalesSummaryId> {

    /**
     * [수정됨]
     * WHERE 절의 필드 경로를 복합 키 구조에 맞게 `u.id.userID`로 수정합니다.
     */
    @Query("""
       SELECT COALESCE(SUM(u.totalRevenue), 0)
         FROM UserSalesSummary u
        WHERE u.id.userID       = :user
          AND u.id.summaryDate >= :startDate
          AND u.id.summaryDate <= :endDate
    """)
    BigDecimal findTotalRevenueByUserIdAndPeriod(
            @Param("user")      Users user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate")   LocalDate endDate
    );

    /**
     * [수정됨]
     * WHERE 절의 필드 경로를 `u.id.userID`와 `u.id.summaryDate`로 수정합니다.
     */
    @Query("""
       SELECT COALESCE(SUM(u.totalRevenue), 0)
         FROM UserSalesSummary u
        WHERE u.id.userID      = :user
          AND u.id.summaryDate = :yesterday
    """)
    BigDecimal findYesterdayRevenueByUserId(
            @Param("user")      Users user,
            @Param("yesterday") LocalDate yesterday
    );

    /**
     * [수정됨]
     * SELECT 및 WHERE 절의 모든 필드 경로를 복합 키 구조에 맞게 수정합니다.
     */
    @Query("""
        SELECT new org.example.prumpt_be.dto.response.EachDaysProfitDto(
                 u.id.userID.userId,
                 u.id.summaryDate,
                 u.totalRevenue
               )
          FROM UserSalesSummary u
         WHERE u.id.userID        = :user
           AND u.id.summaryDate  >= :startDate
           AND u.id.summaryDate  <= :endDate
         ORDER BY u.id.summaryDate
    """)
    List<EachDaysProfitDto> findDailyRevenueByUserAndPeriod(
            @Param("user")      Users user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate")   LocalDate endDate
    );

    /**
     * [수정 불필요]
     * 이 쿼리는 UserSalesSummary가 아닌 PromptPurchase를 조회하므로
     * 기존 코드가 올바릅니다.
     */
    @Query("""
        SELECT COUNT(p)
          FROM PromptPurchase p
         WHERE p.prompt.ownerID = :user
    """)
    long countTotalSalesByUserId(@Param("user") Users user);

    /**
     * [수정됨]
     * Spring Data JPA의 Query Derivation(메소드 이름으로 쿼리 생성)을
     * 복합 키 구조에 맞게 수정한 이름입니다. 이 코드는 올바릅니다.
     */
    Optional<UserSalesSummary> findById_UserIDAndId_SummaryDate(Users userID, LocalDate summaryDate);
}