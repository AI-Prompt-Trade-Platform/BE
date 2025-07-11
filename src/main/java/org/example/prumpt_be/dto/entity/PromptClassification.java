package org.example.prumpt_be.dto.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "prompt_classifications", indexes = {
        @Index(name = "idx_pc_model_category_id", columnList = "model_category_id"),
        @Index(name = "idx_pc_type_category_id", columnList = "type_category_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PromptClassificationId.class) // 복합키 클래스 지정
public class PromptClassification {

    @Id
    @OneToOne(fetch = FetchType.LAZY)

    @JoinColumn(name = "prompt_id", referencedColumnName = "prompt_id")
    @JsonBackReference          // 역참조는 무시
    private Prompt prompt;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", referencedColumnName = "model_id")
    private ModelCategory modelCategory;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", referencedColumnName = "type_id")
    private TypeCategory typeCategory;
}