package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
        List<Prompt> findByOwnerId(Long ownerId);

        // 인기 프롬프트 리스트 (가격 기준, 예시)
        List<Prompt> findTop10ByOrderByPriceDesc();

        // 최신 업로드 프롬프트
        List<Prompt> findTop10ByOrderByCreatedAtDesc();

        // 추천 프롬프트 (모델별)
        List<Prompt> findByModel(String model);

        // 추천 프롬프트 (카테고리별)
        List<Prompt> findByType(String type);

        // 프롬프트 검색
        List<Prompt> findByPromptNameContaining(String keyword);


        List<Prompt> findByPromptNameContainingIgnoreCase(String keyword);
}