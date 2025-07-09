package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "type_categories", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_category_type_slug", columnNames = "type_slug")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "type_id")
    private Integer typeId; // 스키마는 INT

    @Column(name = "type_name", nullable = false, length = 50)
    private String typeName;

    @Column(name = "type_slug", nullable = false, length = 50, unique = true)
    private String typeSlug;

    // PromptClassification과의 관계 (양방향을 위해 추가 가능, 필수는 아님)
    // @OneToMany(mappedBy = "typeCategory")
    // private List<PromptClassification> promptClassifications = new ArrayList<>();
}