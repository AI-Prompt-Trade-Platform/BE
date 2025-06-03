package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.dto.entity.UserSalesSummary;
import org.example.prumpt_be.dto.entity.UserSalesSummaryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSalesSummaryRepository extends JpaRepository<UserSalesSummary, UserSalesSummaryId> {
    Optional<UserSalesSummary> findByUserAndSummaryDate(User user, LocalDate summaryDate);

    // 특정 사용자의 특정 기간 동안의 판매 요약 ( 마이페이지 - 거래 요약 )
    @Query("SELECT uss FROM UserSalesSummary uss WHERE uss.user = :user AND uss.summaryDate BETWEEN :startDate AND :endDate ORDER BY uss.summaryDate DESC")
    List<UserSalesSummary> findByUserAndDateRange(
            @Param("user") User user,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}