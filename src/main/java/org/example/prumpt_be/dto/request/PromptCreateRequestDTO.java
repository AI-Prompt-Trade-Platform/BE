package org.example.prumpt_be.dto.request;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class PromptCreateRequestDTO {

    @JsonProperty("promptName")
    private String title;

    @JsonProperty("promptContent")
    private String content;

    private int price;
    private String aiInspectionRate;
    private String exampleContentUrl;

    private Long modelCategoryId;
    private Long typeCategoryId;

    private List<String> tags;
}


