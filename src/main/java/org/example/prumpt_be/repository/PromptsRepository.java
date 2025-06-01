package org.example.prumpt_be.repository;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.domain.entity.Prompts;
import org.example.prumpt_be.dto.response.PromptDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromptsRepository extends JpaRepository<Prompts, Long> {
    // 프롬프트 전체 조회
    @Query("SELECT p FROM Prompts p")
    List<Prompts> findAllPrompts();

    // 프롬프트 ID로 단건 조회
    @Query("SELECT p FROM Prompts p WHERE p.promptID = :id")
    Prompts findPromptById(@Param("id") Integer id);

    // 프롬프트 생성 (JPQL은 INSERT 지원하지 않으므로, save() 사용)
    Prompts save(Prompts prompt);

    // 프롬프트 수정 (JPQL은 UPDATE 지원, 예시: 이름과 내용만 수정)
    @Modifying
    @Query("UPDATE Prompts p SET p.promptName = :name, p.promptContent = :content WHERE p.promptID = :id")
    int updatePrompt(@Param("id") Integer id, @Param("name") String name, @Param("content") String content);

    // 프롬프트 삭제
    @Modifying
    @Query("DELETE FROM Prompts p WHERE p.promptID = :id")
    int deletePromptById(@Param("id") Integer id);

    // 특정 사용자가 판매중인 프롬프트 조회
    @Query("""
        SELECT new org.example.prumpt_be.dto.response.PromptDto(
            p.promptID,
            p.promptName,
            p.promptContent,
            p.price
        )
        FROM Prompts p
        WHERE p.ownerID.userID = :userId
    """)
    List<PromptDto> findAllByOwnerId(@Param("userId") Integer userId);
}
