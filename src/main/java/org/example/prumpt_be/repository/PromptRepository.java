package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {

    // 최근 등록된 프롬프트 ( 홈 화면 )
    Page<Prompt> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 특정 사용자가 소유한 프롬프트 목록 ( 마이페이지 - 판매중인 프롬프트 )
    Page<Prompt> findByOwnerOrderByCreatedAtDesc(User owner, Pageable pageable);

    // 프롬프트 이름 또는 내용으로 검색 ( 홈 화면 - 검색창 )
    @Query("SELECT p FROM Prompt p WHERE p.promptName LIKE %:keyword% OR p.promptContent LIKE %:keyword% ORDER BY p.createdAt DESC")
    Page<Prompt> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // TODO: 인기 프롬프트 조회 (정의 필요 - 예: 판매량, 조회수, 위시리스트 추가 수 등)
    // 예시: 판매량을 기준으로 (PromptPurchase 테이블과 조인 필요)
    // @Query("SELECT p FROM Prompt p JOIN p.purchases pur GROUP BY p.promptId ORDER BY COUNT(pur.purchaseId) DESC")
    // Page<Prompt> findPopularPrompts(Pageable pageable);
    // 지금은 우선 최근 등록 프롬프트를 인기 프롬프트로 대체하거나, 다른 단순 기준으로 정렬할 수 있습니다.
    // 예를 들어 AI 검수 등급(aiInspectionRate) 같은 필드가 있다면 그걸로 정렬할 수도 있습니다.
    // 여기서는 예시로 createdAt으로 정렬하고, 서비스단에서 이부분을 명확히 인지하고 사용하겠습니다.
    Page<Prompt> findAllByOrderByPriceDesc(Pageable pageable); // 가격 높은 순 (임시 인기 기준)

}