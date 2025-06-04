package org.example.prumpt_be.dto.entity;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "prompt_purchases",
        uniqueConstraints = @UniqueConstraint(columnNames = {"buyer_id", "prompt_id"})
)
public class PromptPurchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "purchase_id")
    private Integer purchaseId;

    @Column(name = "buyer_id")
    private Integer buyerId;

    @Column(name = "prompt_id")
    private Integer promptId;

    @Column(name = "purchased_at")
    private LocalDateTime purchasedAt;
}