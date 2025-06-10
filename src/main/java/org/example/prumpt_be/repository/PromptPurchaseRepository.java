package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.PromptPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromptPurchaseRepository extends JpaRepository<PromptPurchase, Integer> {
    // 특정 사용자가 구매한 프롬프트 목록 ( 마이페이지 - 구매한 프롬프트 )
    Page<PromptPurchase> findByBuyerOrderByPurchasedAtDesc(Users buyer, Pageable pageable);

    // 특정 프롬프트를 특정 사용자가 구매했는지 확인
    boolean existsByBuyerAndPrompt(Users buyer, Prompt prompt);

    // 특정 프롬프트의 구매 내역 (판매자 입장에서 볼 수 있음)
    List<PromptPurchase> findByPromptOrderByPurchasedAtDesc(Prompt prompt);

    // 특정 사용자가 특정 프롬프트를 구매한 내역 조회
    Optional<PromptPurchase> findByBuyerAndPrompt(Users buyer, Prompt prompt);
}