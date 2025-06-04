package org.example.prumpt_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.prumpt_be.dto.entity.Prompts;

// 평가 요청 DTO
@Getter
@Setter
@AllArgsConstructor
public class PromptUploadRequestDto {
    private Integer promptId; // 프롬프트 ID
    private String promptBody;
    private ExampleType exampleType;  // IMAGE, VIDEO, TEXT (enum)
    private String exampleValue;      // S3 URL or 실제 텍스트

    public enum ExampleType {
        IMAGE, VIDEO, TEXT
    }

    public static PromptUploadRequestDto fromEntity(Prompts prompt) {
        return new PromptUploadRequestDto(
                prompt.getPromptID(),
                prompt.getPromptContent(),
                PromptUploadRequestDto.ExampleType.TEXT, // 예시 타입에 맞게 지정
                prompt.getExample_content_url() // 예시 값 (필요시 prompt에서 가져오거나 빈 값)
        );
    }
}