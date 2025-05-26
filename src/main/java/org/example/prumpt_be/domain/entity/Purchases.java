package org.example.prumpt_be.domain.entity;

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
    private int purchaseID;

    @ManyToOne
    @JoinColumn(name = "buyerID", referencedColumnName = "userID", nullable = false)
    private Users buyer;

    @ManyToOne
    @JoinColumn(name = "promptID", referencedColumnName = "promptID", nullable = false)
    private Prompts promptId;

    @Column(nullable = false)
    private LocalDateTime purchasedAt;

    @PrePersist
    public void prePersist() {
        this.purchasedAt = LocalDateTime.now();
    }
}