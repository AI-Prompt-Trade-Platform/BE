package org.example.prumpt_be.repository;


import org.example.prumpt_be.domain.entity.PromptReviews;
import org.example.prumpt_be.dto.response.PromptAvgRateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptReviewsRepository extends JpaRepository<PromptReviews, Integer> {

    //특정 유저의 프롬프트들 전체 별점 평균 조회
    @Query("""
    SELECT AVG(pr.rate)
      FROM PromptReviews pr
     WHERE pr.promptID.ownerID.userID = :userId
""")
    Double findAvgRateOfAllPromptsByUserId(@Param("userId") Integer userId);          //todo: Repo 테스트 (완)


    //특정 유저의 각 프롬프트별 평균별점 조회
    @Query("""
    SELECT new org.example.prumpt_be.dto.response.PromptAvgRateDto(
        pr.promptID.ownerID.userID,
        pr.promptID.promptID,
        pr.promptID.prompt_name,
        AVG(pr.rate)
    )
    FROM PromptReviews pr
    WHERE pr.promptID.ownerID.userID = :userId
    GROUP BY pr.promptID.promptID, pr.promptID.prompt_name, pr.promptID.ownerID.userID
""")
    List<PromptAvgRateDto> findAvgRateByPromptOfUser(@Param("userId") Integer userId);
}
