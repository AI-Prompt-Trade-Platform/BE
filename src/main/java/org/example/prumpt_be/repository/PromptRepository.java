package org.example.prumpt_be.repository;

import org.example.prumpt_be.entity.Prompt;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    @EntityGraph(attributePaths = {"author", "category", "tags", "reviews", "reviews.user"})
    Optional<Prompt> findById(Long id);
}
