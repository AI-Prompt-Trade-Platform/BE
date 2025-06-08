package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prompts", indexes = {
        @Index(name = "idx_prompts_owner_id", columnList = "owner_id"),
        @Index(name = "idx_prompts_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prompt_id")
    private Long promptId; // 스키마는 INT지만 Long 사용 권장

    @Column(name = "prompt_name", nullable = false, length = 255)
    private String promptName;

    @Lob // TEXT 타입 매핑
    @Column(name = "prompt_content", columnDefinition = "TEXT")
    private String promptContent;

    @Column // Nullable
    private Integer price;

    @Column(name = "ai_inspection_rate", length = 255)
    private String aiInspectionRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Users ownerID;

    @Column(name = "example_content_url", length = 255)
    private String exampleContentUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Lob // TEXT 타입 매핑
    @Column(columnDefinition = "TEXT")
    private String model; // AI 모델 정보 (JSON 형태 등으로 저장 가능)

    // PromptClassification 과의 관계 (Prompt가 주인)
    // Prompt 하나는 여러개의 model category와 type category 조합을 가질 수 있는 상황으로 보임 (스키마 상 PK가 prompt_id, model_category_id, type_category_id)
    // 이는 Prompt 입장에서 @OneToMany 관계가 될 것임.
    @OneToMany(mappedBy = "prompt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromptClassification> classifications = new ArrayList<>();

    @OneToMany(mappedBy = "prompt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromptPurchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "prompt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserWishlist> wishlists = new ArrayList<>();

    @OneToMany(mappedBy = "promptID", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromptReview> reviews = new ArrayList<>();
}