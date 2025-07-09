package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.PromptClassification;
import org.example.prumpt_be.dto.entity.PromptClassificationId;
import org.example.prumpt_be.dto.response.ClassificationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PromptClassificationRepository extends JpaRepository<PromptClassification, PromptClassificationId> {
    @Query("""
    SELECT new org.example.prumpt_be.dto.response.ClassificationDTO(
      pc.modelCategory.modelName,
      pc.typeCategory.typeName
    )
    FROM PromptClassification pc
    WHERE pc.prompt.promptId = :promptId
  """)
    ClassificationDTO findDtoByPromptId(@Param("promptId") Long promptId);

    void deleteByPrompt_PromptId(Long promptId);
}