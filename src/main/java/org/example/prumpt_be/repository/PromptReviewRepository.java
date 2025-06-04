package org.example.prumpt_be.repository;

import org.example.prumpt_be.entity.PromptReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface    PromptReviewRepository extends JpaRepository<PromptReview, Long> {
    List<PromptReview> findByPromptPromptId(Long promptId);
}
