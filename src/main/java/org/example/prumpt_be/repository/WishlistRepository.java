package org.example.prumpt_be.repository;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.User;
import org.example.prumpt_be.dto.entity.UserWishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserWishlist 엔티티에 대한 데이터 접근을 처리하는 JpaRepository 인터페이스입니다.
 * 사용자의 위시리스트 관련 데이터베이스 연산을 담당합니다.
 */
@Repository
public interface WishlistRepository extends JpaRepository<UserWishlist, Long> { // UserWishlist의 ID 타입은 Long으로 가정

    /**
     * 특정 사용자와 특정 프롬프트에 해당하는 위시리스트 항목을 조회합니다.
     *
     * @param user 사용자 엔티티
     * @param prompt 프롬프트 엔티티
     * @return Optional<UserWishlist> 해당 위시리스트 항목 (존재하지 않을 수 있음)
     */
    Optional<UserWishlist> findByUserAndPrompt(User user, Prompt prompt);

    /**
     * 특정 사용자의 위시리스트 목록을 추가된 시간(addedAt)의 내림차순으로 페이지네이션하여 조회합니다.
     *
     * @param user 사용자 엔티티
     * @param pageable 페이지네이션 정보 (정렬 정보 포함 가능)
     * @return Page<UserWishlist> 페이징된 위시리스트 항목 목록
     */
    Page<UserWishlist> findByUserOrderByAddedAtDesc(User user, Pageable pageable);

    /**
     * 특정 사용자의 위시리스트에 특정 프롬프트가 존재하는지 확인합니다.
     * 이 메소드는 findByUserAndPrompt().isPresent() 와 유사한 기능을 하지만,
     * COUNT 쿼리를 사용하여 더 효율적일 수 있습니다.
     *
     * @param user 사용자 엔티티
     * @param prompt 프롬프트 엔티티
     * @return boolean 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByUserAndPrompt(User user, Prompt prompt);

    // 필요에 따라 추가적인 조회 메소드를 정의할 수 있습니다. 예를 들어,
    // boolean existsByUser_UserIdAndPrompt_PromptId(Long userId, Long promptId);
    // 위와 같이 ID 기반으로 존재 여부를 확인할 수도 있지만, 객체 기반이 더 명확할 때가 많습니다.
}