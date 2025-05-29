package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromptRepository extends JpaRepository<Prompt, Long> {
    // 필요시 커스텀 쿼리 추가 가능
}
