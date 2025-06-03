package org.example.prumpt_be.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "prompts")
public class Prompts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prompt_id")
    private int promptID;

    @Column(nullable = false)
    private String promptName;

    @Column(columnDefinition = "TEXT")
    private String promptContent;

    @Column(name = "price", nullable = false)
    private int price;

    @OneToMany(mappedBy = "promptID", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PromptReviews> reviews;

    @Column
    private String ai_inspection_rate;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id", nullable = false)
    private Users ownerID;

    @Column
    private String example_content_url;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

}