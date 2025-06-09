package org.example.prumpt_be.repository;
import org.example.prumpt_be.dto.entity.PromptReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface    PromptReviewRepository extends JpaRepository<PromptReview, Long> {
    List<PromptReview> findByPromptPromptID(Long promptId);

//    // 특정 프롬프트에 대한 리뷰 목록 (페이지네이션)
//    Page<PromptReview> findByPromptOrderByReviewedAtDesc(Prompt prompt, Pageable pageable);
//
//    // 특정 사용자가 작성한 리뷰 목록
//    Page<PromptReview> findByReviewerOrderByReviewedAtDesc(Users reviewer, Pageable pageable);
//
//    // 특정 구매 건에 대한 리뷰가 있는지 확인 (리뷰는 구매당 하나)
//    Optional<PromptReview> findByPurchase_PurchaseId(Long purchaseId);
//
//    boolean existsByPurchase_PurchaseId(Long purchaseId);
//
}
