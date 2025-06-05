package org.example.prumpt_be.dto.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

// 복합 키 클래스
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode // 복합키 클래스는 equals와 hashCode 구현 필수
public class PromptClassificationId implements Serializable { // public으로 변경
    private Long prompt; // Prompt 엔티티의 ID 타입과 일치
    private Integer modelCategory; // ModelCategory 엔티티의 ID 타입과 일치
    private Integer typeCategory;  // TypeCategory 엔티티의 ID 타입과 일치
}
