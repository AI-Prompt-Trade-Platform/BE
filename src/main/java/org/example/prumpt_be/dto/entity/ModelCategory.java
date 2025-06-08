package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "model_categories", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_category_model_slug", columnNames = "model_slug")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId; // 스키마는 Int

    @Column(name = "model_name", nullable = false, length = 50)
    private String modelName;

    @Column(name = "model_slug", nullable = false, length = 50, unique = true)
    private String modelSlug;

    // PromptClassification과의 관계 (양방향을 위해 추가 가능, 필수는 아님)
    // @OneToMany(mappedBy = "modelCategory")
    // private List<PromptClassification> promptClassifications = new ArrayList<>();
}