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
    private Integer price; //가격
    private String description; // 프롬프트 설명
    private MultipartFile exampleFile; // 업로드한 이미지, 영상 등
    private PromptUploadRequestDto.ExampleType exampleType;     // 예시 데이터의 타입 ("TEXT", "IMAGE", "VIDEO")
    private Integer modelCategoryIds; // ModelCategory ID 목록
    private Integer typeCategoryIds;  // TypeCategory ID 목록
}