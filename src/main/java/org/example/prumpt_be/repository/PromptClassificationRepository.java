package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.PromptClassification;
import org.example.prumpt_be.dto.entity.PromptClassificationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptClassificationRepository extends JpaRepository<PromptClassification, PromptClassificationId> {
}
