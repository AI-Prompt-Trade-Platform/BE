package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "prompt_reviews", indexes = {
        @Index(name = "idx_reviews_prompt_id", columnList = "prompt_id"),
        @Index(name = "idx_reviews_reviewed_at", columnList = "reviewed_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long reviewId; // 스키마는 INT지만 Long 사용 권장

    // PromptPurchase 와의 1:1 관계. PromptReview 가 대상쪽.
    // PromptPurchase 를 통해서만 리뷰가 생성될 수 있도록 purchase_id 를 FK 로 가짐
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_id", nullable = false, unique = true) // unique = true 로 1:1 관계 강제
    private PromptPurchase purchase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id", nullable = false) // purchase.getPrompt() 로도 접근 가능하지만, denormalization 또는 직접 접근을 위해 유지할 수 있음.
    private Prompt prompt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false) // purchase.getBuyer() 로도 접근 가능.
    private User reviewer;

    @Column // Nullable
    private Double rate;

    @Column(name = "review_content", length = 255)
    private String reviewContent;

    @CreationTimestamp
    @Column(name = "reviewed_at", updatable = false) // 리뷰 작성 시각은 보통 변경되지 않음
    private LocalDateTime reviewedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}