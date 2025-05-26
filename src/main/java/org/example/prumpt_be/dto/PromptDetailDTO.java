package org.example.prumpt_be.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromptDetailDTO {
    private Long promptId;
    private String title;
    private String description;
    private String content;
    private int price;
    private String authorName;
    private String authorProfile;
    private String categoryName;
    private List<String> tags;
    private boolean wishList;
    private double averageRating;
    private List<ReviewDTO> reviews;
}
