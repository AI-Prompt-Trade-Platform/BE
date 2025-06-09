package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.PromptClassification;
import org.example.prumpt_be.dto.entity.PromptClassificationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PromptClassificationRepository extends JpaRepository<PromptClassification, PromptClassificationId> {
    List<PromptClassification> findByPrompt_PromptId(Long promptId);
    List<PromptClassification> findByModelCategory_ModelId(Integer modelId);
    List<PromptClassification> findByTypeCategory_TypeId(Integer typeId);
}