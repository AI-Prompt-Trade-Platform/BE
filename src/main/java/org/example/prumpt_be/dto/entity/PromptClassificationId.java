package org.example.prumpt_be.dto.entity;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

// 복합 키 클래스
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode // 복합키 클래스는 equals와 hashCode 구현 필수
public class PromptClassificationId implements Serializable {
    private Long prompt;
    private Integer modelCategory;
    private Integer typeCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromptClassificationId that)) return false;
        return Objects.equals(prompt, that.prompt)
            && Objects.equals(modelCategory, that.modelCategory)
            && Objects.equals(typeCategory, that.typeCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prompt, modelCategory, typeCategory);
    }
}