package org.example.prumpt_be.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReviewDTO {
    private String username;
    private Double rating;
    private String content;

    public ReviewDTO(String username, Double rating, String content) {
        this.username = username;
        this.rating   = rating;
        this.content  = content;
    }
}
