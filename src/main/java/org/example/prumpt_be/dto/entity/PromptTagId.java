package org.example.prumpt_be.dto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


//promptTag 엔티티 복합키 클래스
@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PromptTagId implements Serializable {

    @Column(name = "prompt_id")
    private Long promptId;

    @Column(name = "tag_id")
    private Long tagId;

    public PromptTagId(Long promptId, Long tagId) {
        this.promptId = promptId;
        this.tagId = tagId;
    }
}