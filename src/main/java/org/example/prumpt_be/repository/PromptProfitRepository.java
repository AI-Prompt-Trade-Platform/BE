package org.example.prumpt_be.repository;

import org.example.prumpt_be.domain.entity.UserSalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// UserSalesSummaryRepository 와 겹쳐서 일단 그대로 둠
@Repository
public interface PromptProfitRepository extends JpaRepository<UserSalesSummary, Long> {

    //입력받은 userId가 owner인 프롬프트들의 총 판매 금액 조히
    @Query("SELECT SUM(pu.promptId.price) FROM Purchases pu WHERE pu.promptId.ownerID.userID = :userId")
    Integer findTotalProfitByOwnerId(@Param("userId") int userId);

    //유저ID와 판매자ID가 동일한 상품들의 전체 별점 평균 조회
    @Query(value = "SELECT AVG(avg_rate) FROM (" +
            "SELECT AVG(rate) as avg_rate FROM prompt_reviews r " +
            "JOIN prompts p ON r.promptId = p.promptID " +
            "WHERE p.owner_id = :userId " +
            "GROUP BY r.promptId" +
            ") as sub", nativeQuery = true)
    Double findAvgOfPromptAvgRatesByUserId(@Param("userId") int userId);
}
