package org.example.prumpt_be.dto.entity;

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
    @Column(name = "review_id")
    private int reviewID;
    @OneToOne
    @JoinColumn(name = "purchase_id", referencedColumnName = "purchase_id", nullable = false, unique = true)
    private Purchases purchaseID;
    @ManyToOne
    @JoinColumn(name = "prompt_id", referencedColumnName = "prompt_id", nullable = false)
    private Prompts promptID;
    @ManyToOne
    @JoinColumn(name = "reviewer_id", referencedColumnName = "user_id", nullable = false)
    private Users reviewerID;
    @Column
    private Double rate;
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


    public void setPromptId(Prompts prompt) {
        this.promptID = prompt;
    }
    public void setPurchaseId(Purchases purchase) {
        this.purchaseID = purchase;
    }
    public void setReviewer(Users reviewer) {
        this.reviewerID = reviewer;
    }

}
