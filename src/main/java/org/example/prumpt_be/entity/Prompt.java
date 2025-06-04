package org.example.prumpt_be.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "prompts")
public class Prompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promptid")
    private Long promptId;

    @Column(name = "prompt_name", nullable = false) // ✅ 정확히 DB 컬럼명과 일치시켜야 함
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "prompt_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "price")
    private int price;

    @Column(name = "ai_inspection_rate")
    private String aiInspectionRate;

    @Column(name = "example_content_url")
    private String exampleContentUrl;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id", nullable = false)
    private User owner;

    @OneToOne(mappedBy = "prompt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PromptClassification classification;

    @ManyToMany
    @JoinTable(
            name = "prompt_tag",
            joinColumns = @JoinColumn(name = "prompt_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "prompt")
    private List<Review> reviews;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
