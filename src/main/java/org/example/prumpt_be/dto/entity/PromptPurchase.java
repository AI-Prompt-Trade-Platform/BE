package org.example.prumpt_be.dto.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "prompt_purchases",
       uniqueConstraints = @UniqueConstraint(columnNames = {"buyer_id", "prompt_id"},
       indexes = {
        @Index(name = "idx_purchases_buyer_id", columnList = "buyer_id"),
        @Index(name = "idx_purchases_prompt_id", columnList = "prompt_id"),
        @Index(name = "idx_purchases_purchased_at", columnList = "purchased_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromptPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Interger purchaseId; // 스키마는 INT지만 Long 사용 권장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Users buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id", nullable = false)
    private Prompt prompt;

    @CreationTimestamp
    @Column(name = "purchased_at", nullable = false, updatable = false)
    private LocalDateTime purchasedAt;

    // PromptReview 와의 1:1 관계 (PromptPurchase 가 주인)
    @OneToOne(mappedBy = "purchase", cascade = CascadeType.ALL)
    private PromptReview review;
}