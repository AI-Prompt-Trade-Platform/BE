package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.ModelCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModelCategoryRepository extends JpaRepository<ModelCategory, Integer> {
    Optional<ModelCategory> findByModelSlug(String modelSlug);
    Optional<ModelCategory> findByModelName(String modelName);
}