package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.UserSalesSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface UserSalesSummaryRepository extends JpaRepository<UserSalesSummary, Integer> {
    Optional<UserSalesSummary> findByUserIdAndSummaryDate(Integer userId, LocalDate summaryDate);
}