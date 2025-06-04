package org.example.prumpt_be.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PromptUpdateRequestDto {
    private String promptName;
    private String promptContent;
    private Integer price;
    private String exampleContentUrl;
    private String modelInfo;
    private List<Integer> modelCategoryIds;
    private List<Integer> typeCategoryIds;
}