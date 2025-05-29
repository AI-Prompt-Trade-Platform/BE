package org.example.prumpt_be.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "prompts")
public class Prompt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prompt_id")
    private Integer promptId;

    @Column(name = "prompt_name")
    private String promptName;

    @Column(name = "prompt_content")
    private String promptContent;

    @Column(name = "price")
    private Integer price;

    @Column(name = "ai_inspection_rate")
    private String aiInspectionRate;

    @Column(name = "owner_id")
    private Integer ownerId;

    @Column(name = "example_content_url")
    private String exampleContentUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}