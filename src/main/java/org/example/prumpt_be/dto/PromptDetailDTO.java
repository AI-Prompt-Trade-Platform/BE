package org.example.prumpt_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PromptDetailDTO {
    private Long id;
    private String title;
    private String description;
    private String content;
    private int price;
    private String authorName;
    private String authorProfile;
    private String category;
    private List<String> tags;
    private boolean isBookmarked;
    private double averageRating;
    private List<ReviewDTO> reviews;

    private String modelName;
    private String typeName;
}
