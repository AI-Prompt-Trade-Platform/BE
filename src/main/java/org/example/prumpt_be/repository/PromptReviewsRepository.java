package org.example.prumpt_be.repository;


import org.example.prumpt_be.dto.entity.PromptReview;
import org.example.prumpt_be.dto.response.PromptAvgRateDto;
import org.example.prumpt_be.dto.response.RateAvgDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptReviewsRepository extends JpaRepository<PromptReview, Integer> {


//================================조회=========================================
    //특정 유저의 프롬프트들 전체 별점 평균 조회
@Query("""
        SELECT new org.example.prumpt_be.dto.response.RateAvgDto(
            pr.promptID.ownerID.userId,
            AVG(pr.rate)
        )
          FROM PromptReview pr
         WHERE pr.promptID.ownerID.userId = :userId
         GROUP BY pr.promptID.ownerID.userId
    """)
RateAvgDto findAvgRateOfAllPromptsByUserId(@Param("userId") int userId);          //todo: Repo 테스트 (완)


    //특정 유저의 각 프롬프트별 평균별점 리스트 조회
    @Query("""
    SELECT new org.example.prumpt_be.dto.response.PromptAvgRateDto(
        pr.promptID.ownerID.userId,
        pr.promptID.promptId,
        pr.promptID.promptName,
        AVG(pr.rate)
    )
    FROM PromptReview pr
    WHERE pr.promptID.ownerID.userId = :userId
    GROUP BY pr.promptID.promptId, pr.promptID.promptName, pr.promptID.ownerID.userId
""")
    List<PromptAvgRateDto> findAvgRateByPromptOfUser(@Param("userId") Integer userId);

//==========================================================================

//==================================삽입=========================================


//===============================================================================
}
