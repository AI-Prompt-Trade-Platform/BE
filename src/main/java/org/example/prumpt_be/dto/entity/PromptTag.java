package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프롬프트와 태그의 관계를 정의하는 엔티티. (다대다 관계의 조인 테이블)
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "prompt_tag", indexes = {
    @Index(name = "idx_pt_prompt_id", columnList = "prompt_id"),
    @Index(name = "idx_pt_tag_id", columnList = "tag_id")
})
public class PromptTag {

    @EmbeddedId
    private PromptTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("promptId") // PromptTagId의 promptId 필드와 매핑
    @JoinColumn(name = "prompt_id")
    private Prompt prompt;



    /**
     * Tag 엔티티와의 다대일(N:1) 관계를 정의.
     * PromptTagId의 tagId 필드에 매핑.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId") // PromptTagId의 tagId 필드와 매핑
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PromptTag(Prompt prompt, Tag tag) {
        this.prompt = prompt;
        this.tag = tag;
        this.id = new PromptTagId(prompt.getPromptId(), tag.getId());
    }
}