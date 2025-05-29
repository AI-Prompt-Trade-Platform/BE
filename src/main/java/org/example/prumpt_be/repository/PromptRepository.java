package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    List<Prompt> findByOwnerId(Long ownerId);
}