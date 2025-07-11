package org.example.prumpt_be.repository;


import org.example.prumpt_be.dto.ReviewDTO;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.PromptReview;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.response.PromptAvgRateDto;
import org.example.prumpt_be.dto.response.RateAvgDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromptReviewsRepository extends JpaRepository<PromptReview, Long> {


//================================조회=========================================
// [수정됨] int userId 대신 Users 객체를 직접 받도록 변경
@Query("""
            SELECT new org.example.prumpt_be.dto.response.RateAvgDto(
                pr.promptID.ownerID.userId,
                AVG(pr.rate)
            )
              FROM PromptReview pr
             WHERE pr.promptID.ownerID = :user
             GROUP BY pr.promptID.ownerID.userId
        """)
RateAvgDto findAvgRateOfAllPromptsByUserId(@Param("user") Users user);


    // [수정됨] Integer userId 대신 Users 객체를 직접 받도록 변경
    @Query("""
        SELECT new org.example.prumpt_be.dto.response.PromptAvgRateDto(
            pr.promptID.ownerID.userId,
            pr.promptID.promptId,
            pr.promptID.promptName,
            AVG(pr.rate)
        )
        FROM PromptReview pr
        WHERE pr.promptID.ownerID = :user
        GROUP BY pr.promptID.promptId, pr.promptID.promptName, pr.promptID.ownerID.userId
    """)
    List<PromptAvgRateDto> findAvgRateByPromptOfUser(@Param("user") Users user);

    // 특정 프롬프트의 리뷰 전체 조회 (리뷰 DTO로 변환)
    @Query("SELECT new org.example.prumpt_be.dto.ReviewDTO(" +
            "   pr.reviewer.profileName," +      // Users 엔티티의 프로필 이름
            "   pr.rate," +                     // PromptReview.rate (Double)
            "   pr.reviewContent" +             // 리뷰 내용
            ") " +
            "FROM PromptReview pr " +
            "WHERE pr.promptID.promptId = :promptId"
    )
    List<ReviewDTO> findAllByPromptIdAsDTO(@Param("promptId") Long promptId);

    Optional<PromptReview> findByReviewIdAndReviewerUserId(Long reviewId, Integer userId);

    // 특정 프롬프트의 평균 별점 조회
    @Query("SELECT AVG(pr.rate) FROM PromptReview pr WHERE pr.promptID.promptId = :promptId")
    Optional<Double> findAverageRateByPromptId(@Param("promptId") Long promptId);

    boolean existsByReviewerAndPromptID(Users reviewer, Prompt prompt);

//==========================================================================

//==================================삽입=========================================


//===============================================================================
}
