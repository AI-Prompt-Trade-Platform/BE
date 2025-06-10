package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.response.PromptDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PromptsRepository extends JpaRepository<Prompt, Long> {
    // 프롬프트 전체 조회
    @Query("SELECT p FROM Prompt p")
    List<Prompt> findAllPrompts();

    // 프롬프트 ID로 단건 조회
    @Query("SELECT p FROM Prompt p WHERE p.promptId = :id")
    Prompt findPromptById(@Param("id") Integer id);

    // 프롬프트 수정 (JPQL은 UPDATE 지원, 예시: 이름과 내용만 수정)
    @Modifying
    @Query("UPDATE Prompt p SET p.promptName = :name, p.promptContent = :content WHERE p.promptId = :id")
    int updatePrompt(@Param("id") Integer id, @Param("name") String name, @Param("content") String content);

    // 프롬프트 삭제
    @Modifying
    @Query("DELETE FROM Prompt p WHERE p.promptId = :id")
    int deletePromptById(@Param("id") Integer id);

    // 특정 사용자가 판매중인 프롬프트 조회
    @Query("""
        SELECT new org.example.prumpt_be.dto.response.PromptDto(
            p.promptId,
            p.promptName,
            p.promptContent,
            p.price
        )
        FROM Prompt p
        WHERE p.ownerID.userId = :userId
    """)
    List<PromptDto> findAllByOwnerId(@Param("userId") Integer userId);


    @Query("""
        SELECT p.ownerID.userId
          FROM Prompt p
         WHERE p.promptId   = :promptId
           AND p.ownerID.auth0Id = :auth0Id
    """)
    Optional<Users> findOwnerUserIdByPromptIdAndOwnerAuth0Id(
            @Param("promptId") Long promptId,
            @Param("auth0Id")  String auth0Id
    );
}
