package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.PromptTag;
import org.example.prumpt_be.dto.entity.PromptTagId;
import org.example.prumpt_be.dto.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PromptTag 엔티티를 위한 Spring Data JPA 리포지토리 인터페이스입니다.
 */
@Repository
public interface PromptTagRepository extends JpaRepository<PromptTag, PromptTagId> {

    /**
     * 특정 프롬프트에 연결된 모든 Tag 엔티티 목록을 조회하는 JPA
     * @param promptId 프롬프트의 ID
     * @return 해당 프롬프트와 연결된 Tag 엔티티의 리스트
     */
    @Query("SELECT pt.tag FROM PromptTag pt WHERE pt.prompt.promptId = :promptId")
    List<Tag> findTagsByPromptId(@Param("promptId") Integer promptId);

    // --- 다른 방법 (참고용) ---
    // 아래와 같이 메서드 이름으로 쿼리를 생성할 수도 있지만,
    // 이 경우 PromptTag 리스트를 반환하므로 서비스 계층에서 추가적인 변환이 필요합니다.
    // List<PromptTag> findByPrompt_PromptId(Integer promptId);
}