package org.example.prumpt_be.dto.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "example_content_url", length = 255)
    private String exampleContentUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "model", columnDefinition = "TEXT")
    private String model; // AI 모델 정보 (JSON 형태 등으로 저장 가능)

    @OneToOne(mappedBy = "prompt", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference       // 직렬화할 때 이쪽만 반환(순환 참조 방지)
    private PromptClassification classifications;

    @OneToMany(mappedBy = "prompt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromptPurchase> purchases = new ArrayList<>();

    @OneToMany(mappedBy = "prompt", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserWishlist> wishlists = new ArrayList<>();

    @OneToMany(mappedBy = "promptID", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PromptReview> reviews = new ArrayList<>();

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; //썸네일에서 보기 편하게 하기위한 필드

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id", nullable = false)
    private Users ownerID;

    @ManyToMany
    @JoinTable(
            name = "prompt_tag",
            joinColumns = @JoinColumn(name = "prompt_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;
}
