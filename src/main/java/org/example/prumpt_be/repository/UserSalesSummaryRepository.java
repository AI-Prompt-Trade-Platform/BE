package org.example.prumpt_be.repository;

import org.example.prumpt_be.domain.entity.UserSalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

@Repository
public interface UserSalesSummaryRepository
    extends JpaRepository<UserSalesSummary, Integer> {

    //지정된 기간(startDate ~ endDate) 동안의 판매 합계 조회 메소드
    //데이터 없으면 0 반환
    @Query("""
       SELECT COALESCE(SUM(u.totalRevenue), 0)
         FROM UserSalesSummary u
        WHERE u.user.userID   = :userId
          AND u.summaryDate  >= :startDate
          AND u.summaryDate  <= :endDate
    """)
    BigDecimal findTotalRevenueByUserIdAndPeriod(
            @Param("userId")    Integer  userId, //사용자Id 입력
            @Param("startDate") LocalDate startDate, //조회 기간 시작일
            @Param("endDate")   LocalDate endDate //조회 기간 종료일
    );

    //조회한 날의 어제자 수익 반환 메소드
    default BigDecimal findTotalRevenueByUserIdAndPeriod(Integer userId) {
        // Asia/Seoul 타임존 기준, 어제 날짜만 계산
        LocalDate yesterday = LocalDate
                .now(ZoneId.of("Asia/Seoul"))
                .minusDays(1);
        return findTotalRevenueByUserIdAndPeriod(userId, yesterday, yesterday);
    }

}