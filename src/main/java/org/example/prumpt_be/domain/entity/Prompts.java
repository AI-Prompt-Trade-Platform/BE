package org.example.prumpt_be.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "prompts")
public class Prompts {
    @Id
    @Column(name = "promptID")
    private Integer promptID;

    @Column(nullable = false)
    private String prompt_name;

    @Column(columnDefinition = "TEXT")
    private String prompt_content;

    @Column
    private int price;

    @Column
    private String ai_inspection_rate;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "userID", nullable = false, unique = true)
    private Users ownerID;

    @Column
    private String example_content_url;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;
}