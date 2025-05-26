package org.example.prumpt_be.dto.request;

import lombok.Data;

@Data
public class ReviewCreateRequestDTO {
    private Long promptId;
    private int rating;
    private String content;
}
