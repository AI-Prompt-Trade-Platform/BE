package org.example.prumpt_be.repository;

import org.example.prumpt_be.domain.entity.PromptSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromptProfitRepository extends JpaRepository<PromptSales, Long> {

    //유저ID와 판매자ID가 동일한 상품들의 금액 총액 조회
    @Query("SELECT SUM(p.totalProfit) FROM PromptSales p WHERE p.userID = :userId")
    Integer findTotalProfitByUserId(@Param("userId") int userId);

    //유저ID와 판매자ID가 동일한 상품들의 전체 별점 평균 조회
    @Query("SELECT AVG(p.rateAvg) FROM PromptReviews p WHERE p.userID = :userId")
    float findAvgRatingByUserId(@Param("userId") int userId);
}
