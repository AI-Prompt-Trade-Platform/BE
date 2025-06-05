package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.TypeCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeCategoryRepository extends JpaRepository<TypeCategory, Integer> {
    Optional<TypeCategory> findByTypeSlug(String typeSlug);
    Optional<TypeCategory> findByTypeName(String typeName);
}