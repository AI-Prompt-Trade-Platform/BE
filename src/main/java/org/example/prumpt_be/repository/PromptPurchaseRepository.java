package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptPurchaseRepository extends JpaRepository<PromptPurchase, Integer> {
    boolean existsByBuyerIdAndPromptId(Integer buyerId, Integer promptId);
}