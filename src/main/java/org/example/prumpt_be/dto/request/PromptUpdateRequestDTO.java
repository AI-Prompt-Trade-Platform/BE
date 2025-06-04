// dto/request/PromptUpdateRequestDTO.java
package org.example.prumpt_be.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class PromptUpdateRequestDTO {
    @JsonProperty("promptName")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("promptContent")
    private String content;

    private int price;

    @JsonProperty("exampleContentUrl")
    private String exampleContentUrl;

    @JsonProperty("aiInspectionRate")
    private String aiInspectionRate;

    private Long categoryId;
    private Long modelCategoryId;
    private List<String> tags;
}
