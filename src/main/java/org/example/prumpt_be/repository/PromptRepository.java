package org.example.prumpt_be.repository;

import org.example.prumpt_be.entity.Prompt;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PromptRepository extends JpaRepository<Prompt, Long> {

    @EntityGraph(attributePaths = {
            "owner", "tags", "classification",
            "classification.modelCategory", "classification.typeCategory"
    })
    List<Prompt> findAll();

    @EntityGraph(attributePaths = {
            "owner", "tags", "classification",
            "classification.modelCategory", "classification.typeCategory"
    })
    Optional<Prompt> findById(Long id);

}

