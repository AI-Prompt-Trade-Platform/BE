package org.example.prumpt_be.repository;


import org.example.prumpt_be.dto.entity.UserSalesSummary;
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

// todo: 수익요약 Repository (필수)
@Repository
public interface UserSalesSummaryRepository
    extends JpaRepository<UserSalesSummary, UserSalesSummaryId> {

    //지정된 기간(startDate ~ endDate) 동안의 판매 금액 조회
    //데이터 없으면 0 반환
    @Query("""
       SELECT COALESCE(SUM(u.totalRevenue), 0)
         FROM UserSalesSummary u
        WHERE u.userID.userId   = :userId
          AND u.summaryDate  >= :startDate
          AND u.summaryDate  <= :endDate
    """)
    BigDecimal findTotalRevenueByUserIdAndPeriod(                  //todo: Repo 테스트 (완)
            @Param("userId")    int  userId, //사용자Id 입력
            @Param("startDate") LocalDate startDate, //조회 기간 시작일
            @Param("endDate")   LocalDate endDate //조회 기간 종료일
    );

    //조회한 날의 어제자 수익 반환 메소드
    @Query("""
   SELECT COALESCE(SUM(u.totalRevenue), 0)
     FROM UserSalesSummary u
    WHERE u.userID.userId  = :userID
      AND u.summaryDate  = :yesterday
""")
    BigDecimal findYesterdayRevenueByUserId(               //todo: Repo 테스트 (완)
            @Param("userID") int userID,
            @Param("yesterday") LocalDate yesterday //Service 계층에서 어제날짜를 입력 해줘야함
    );

    //특정 기간동안 수익이 있던 날들의 데이터 출력 (차트 그리기용)
    @Query("""
    SELECT new org.example.prumpt_be.dto.response.EachDaysProfitDto(
             u.userID.userId,
             u.summaryDate,
             u.totalRevenue
           )
      FROM UserSalesSummary u
     WHERE u.userID.userId   = :userID
       AND u.summaryDate     >= :startDate
       AND u.summaryDate     <= :endDate
     ORDER BY u.summaryDate
""")
    List<EachDaysProfitDto> findDailyRevenueByUserAndPeriod(               //todo: Repo 테스트 (완)
            @Param("userID")    int  userID,
            @Param("startDate") LocalDate startDate,
            @Param("endDate")   LocalDate endDate
    );

    //특정 사용자의 총 판매 건수 조회
    @Query("""
    SELECT COUNT(p)
      FROM PromptPurchase p
     WHERE p.prompt.ownerID.userId = :userID
""")
    long countTotalSalesByUserId(@Param("userID") int userID);

    @Query("SELECT u FROM UserSalesSummary u WHERE u.userID.userId = :userId AND u.summaryDate = :summaryDate")
    Optional<UserSalesSummary> findByUserIDAndSummaryDate(
            @Param("userId") Integer userId,
            @Param("summaryDate") LocalDate summaryDate
    );

}