package org.example.prumpt_be.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromptReviewRequestDTO {
    private Long promptId;
    private Long userId;
    private Double rating;
    private String content;
}
