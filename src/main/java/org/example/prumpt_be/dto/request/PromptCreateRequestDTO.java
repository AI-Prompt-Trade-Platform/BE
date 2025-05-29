package org.example.prumpt_be.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class PromptCreateRequestDTO {
    private String title;
    private String content;
    private int price;
    private String aiInspectionRate;
    private String exampleContentUrl;

    private Long modelCategoryId;  // 모델 카테고리
    private Long typeCategoryId;   // 프롬프트 타입

    private List<String> tags;


}

