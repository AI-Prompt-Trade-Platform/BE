package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.dto.entity.UserWishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWishlistRepository extends JpaRepository<UserWishlist, Long> {
    // 특정 사용자의 위시리스트 (페이지네이션 적용 가능)
    Page<UserWishlist> findByUserOrderByAddedAtDesc(User user, Pageable pageable);

    Optional<UserWishlist> findByUserAndPrompt(User user, Prompt prompt);

    boolean existsByUserAndPrompt_PromptId(User user, Long promptId);
}