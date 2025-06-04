package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "prompt_purchases")
public class Purchases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int purchase_id;

    @ManyToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "user_id", nullable = false)
    private Users buyer;

    @ManyToOne
    @JoinColumn(name = "prompt_id", referencedColumnName = "prompt_id", nullable = false)
    private Prompts promptId;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    @PrePersist
    public void prePersist() {
        this.purchasedAt = LocalDateTime.now();
    }
}