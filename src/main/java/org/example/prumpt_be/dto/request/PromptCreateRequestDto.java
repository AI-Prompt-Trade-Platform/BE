package org.example.prumpt_be.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PromptCreateRequestDto {
    private String promptName;
    private String promptContent;
    private Integer price;
    // private String aiInspectionRate; // 보통 시스템에서 설정하거나 별도 프로세스
    private String exampleContentUrl;
    private String modelInfo; // AI 모델 정보 (엔티티의 'model' 필드)
    private List<Integer> modelCategoryIds; // ModelCategory ID 목록
    private List<Integer> typeCategoryIds;  // TypeCategory ID 목록
}