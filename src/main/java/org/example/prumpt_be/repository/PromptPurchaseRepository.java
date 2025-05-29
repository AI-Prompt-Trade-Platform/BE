package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromptPurchaseRepository extends JpaRepository<PromptPurchase, Long> {
    List<PromptPurchase> findByBuyerUserIdAndStatus(Long userId, String status);
}