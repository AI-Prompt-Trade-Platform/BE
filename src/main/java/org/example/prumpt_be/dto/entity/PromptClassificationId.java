package org.example.prumpt_be.dto.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PromptClassificationId implements Serializable {
    private Long prompt;
    private Long modelCategory;
    private Long typeCategory;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PromptClassificationId)) return false;
        PromptClassificationId that = (PromptClassificationId) o;
        return Objects.equals(prompt, that.prompt)
            && Objects.equals(modelCategory, that.modelCategory)
            && Objects.equals(typeCategory, that.typeCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prompt, modelCategory, typeCategory);
    }
}
