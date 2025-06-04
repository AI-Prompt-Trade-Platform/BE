package org.example.prumpt_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDTO {
    private String username;
    private int rating;
    private String content;
}
