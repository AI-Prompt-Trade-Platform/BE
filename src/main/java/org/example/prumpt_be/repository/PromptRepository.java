package org.example.prumpt_be.repository;
import java.util.List;
import java.util.Optional;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Prompt 엔티티에 대한 데이터 접근을 처리하는 JpaRepository 인터페이스입니다.
 * 프롬프트의 기본적인 CRUD 연산 외에 다양한 조건의 프롬프트 조회를 담당합니다.
 */


//todo: 이거말고 promptsRepository로 전부 변경
@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
  

    List<Prompt> findAll();


    Optional<Prompt> findById(Long id);

    // 최근 등록된 프롬프트 (홈 화면) -> Pageable에 정렬 정보가 있으므로 findAll로 대체 가능
    // Page<Prompt> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 특정 사용자가 소유한 프롬프트 목록 (마이페이지 - 판매중인 프롬프트)
    Page<Prompt> findByOwnerIDOrderByCreatedAtDesc(Users owner, Pageable pageable);

    // 프롬프트 이름 또는 내용으로 검색 (홈 화면 - 검색창)
    @Query("SELECT p FROM Prompt p WHERE p.promptName LIKE %:keyword% OR p.promptContent LIKE %:keyword%")
    Page<Prompt> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // (임시 인기 기준) 가격 높은 순 -> Pageable에 정렬 정보가 있으므로 findAll로 대체 가능
    // Page<Prompt> findAllByOrderByPriceDesc(Pageable pageable);


    /**
     * 모델 카테고리 슬러그 및/또는 타입 카테고리 슬러그로 프롬프트를 필터링하여 조회합니다.
     * 두 슬러그 모두 null이거나 비어있으면 필터링 없이 모든 프롬프트를 반환합니다 (Pageable의 정렬은 적용됨).
     *
     * @param modelCategorySlug 필터링할 모델 카테고리 슬러그 (선택 사항)
     * @param typeCategorySlug 필터링할 타입 카테고리 슬러그 (선택 사항)
     * @param pageable 페이지네이션 및 정렬 정보
     * @return 필터링된 프롬프트 목록 (페이지네이션 적용)
     */
    @Query("SELECT DISTINCT p FROM Prompt p LEFT JOIN p.classifications pc " +
           "LEFT JOIN pc.modelCategory mc LEFT JOIN pc.typeCategory tc " +
           "WHERE (:modelCategorySlug IS NULL OR mc.modelSlug = :modelCategorySlug) " +
           "AND (:typeCategorySlug IS NULL OR tc.typeSlug = :typeCategorySlug)")
    Page<Prompt> findPromptsByCategories(
            @Param("modelCategorySlug") String modelCategorySlug,
            @Param("typeCategorySlug") String typeCategorySlug,
            Pageable pageable
    );

    @Query("""
        SELECT p.ownerID.userId
          FROM Prompt p
         WHERE p.promptId   = :promptId
           AND p.ownerID.auth0Id = :auth0Id
    """)
    Optional<Integer> findOwnerUserIdByPromptIdAndOwnerAuth0Id(
            @Param("promptId") Long promptId,
            @Param("auth0Id")  String auth0Id
    );

    List<Prompt> findByOwnerID(Users user);
}