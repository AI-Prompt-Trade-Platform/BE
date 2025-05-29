package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.UserWishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<UserWishlist, Long> {
    List<UserWishlist> findByUserId(Long userId);
    boolean existsByUserIdAndPromptId(Long userId, Long promptId);
    Optional<UserWishlist> findByUserIdAndPromptId(Long userId, Long promptId);
    void deleteByUserIdAndPromptId(Long userId, Long promptId);
}