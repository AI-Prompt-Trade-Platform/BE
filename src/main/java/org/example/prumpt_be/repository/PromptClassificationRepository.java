package org.example.prumpt_be.repository;

import org.example.prumpt_be.entity.PromptClassification;
import org.example.prumpt_be.entity.PromptClassificationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptClassificationRepository extends JpaRepository<PromptClassification, PromptClassificationId> {
}
