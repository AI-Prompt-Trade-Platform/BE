package org.example.prumpt_be.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "prompt_purchases")
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