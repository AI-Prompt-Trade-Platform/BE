package org.example.prumpt_be.repository;

import org.example.prumpt_be.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Integer> {
}