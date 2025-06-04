package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PromptClassificationId.class)
@Table(name = "prompt_classifications")
public class PromptClassification {

    @Id
    @OneToOne
    @JoinColumn(name = "prompt_id")
    private Prompt prompt;

    @Id
    @ManyToOne
    @JoinColumn(name = "model_category_id")
    private ModelCategory modelCategory;

    @Id
    @ManyToOne
    @JoinColumn(name = "type_category_id")
    private TypeCategory typeCategory;
}

