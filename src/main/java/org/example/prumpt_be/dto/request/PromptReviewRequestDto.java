package org.example.prumpt_be.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromptReviewRequestDto {
    private Integer userId;
    private Long promptId;
    private Double rate;
    private String reviewContent;
}