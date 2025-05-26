package org.example.prumpt_be.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "prompt_reviews")
public class PromptReviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewID;
    @OneToOne
    @JoinColumn(name = "purchaseId", referencedColumnName = "purchaseID", nullable = false, unique = true)
    private Purchases purchaseId;
    @ManyToOne
    @JoinColumn(name = "promptId", referencedColumnName = "promptID", nullable = false)
    private Prompts promptId;
    @Column
    private int rate;
    @Column
    private String reviewContent;
    @Column
    private LocalDateTime reviewedAt;
    @Column
    private LocalDateTime updatedAt;


    @PrePersist //엔티티가 저장되기 전 실행
    public void prePersist() {
        this.reviewedAt = java.time.LocalDateTime.now(); //현재 시간
    }

    @PreUpdate //엔티티가 수정되기 전 실행
    public void preUpdate() {
        this.updatedAt = java.time.LocalDateTime.now();
    }
}
