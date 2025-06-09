package org.example.prumpt_be.dto.entity;

import lombok.*;
import jakarta.persistence.*;
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
    private Long promptID;

    @Column(name = "prompt_name", nullable = false)
    private String promptName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; //썸네일에서 보기 편하게 하기위한 필드

    @Column(name = "prompt_content", columnDefinition = "TEXT")
    private String promptContent;

    @Column(name = "price")
    private int price;

    @Column(name = "ai_inspection_rate")
    private String aiInspectionRate;

    @Column(name = "example_content_url")
    private String exampleContentUrl;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id", nullable = false)
    private Users ownerID;

    @OneToOne(mappedBy = "prompt", cascade = CascadeType.ALL)
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

