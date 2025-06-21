package org.example.prumpt_be.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile; // MultipartFile 임포트
import java.util.List;

@Getter
@Setter
public class PromptCreateRequestDto {
    private String promptName;
    private String promptContent;
    private Integer price;
    private String exampleContent;
    private String description;
    // 파일(이미지, 영상, 텍스트 파일)을 업로드할 경우 사용
    private MultipartFile exampleFile;
    // 예시 데이터의 타입 ("TEXT", "IMAGE", "VIDEO")
    private PromptUploadRequestDto.ExampleType exampleType;
    private String modelInfo; // AI 모델 정보 (엔티티의 'model' 필드)
    private List<Integer> modelCategoryIds; // ModelCategory ID 목록
    private List<Integer> typeCategoryIds;  // TypeCategory ID 목록
}