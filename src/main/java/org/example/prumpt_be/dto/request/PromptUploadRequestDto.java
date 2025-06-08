package org.example.prumpt_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.prumpt_be.dto.entity.Prompt;


// 평가 요청 DTO
@Getter
@Setter
@AllArgsConstructor
public class PromptUploadRequestDto {
    private Long promptId; // 프롬프트 ID
    private String promptBody;
    private ExampleType exampleType;  // IMAGE, VIDEO, TEXT (enum)
    private String exampleValue;      // S3 URL or 실제 텍스트

    public enum ExampleType {
        IMAGE, VIDEO, TEXT
    }

    public static PromptUploadRequestDto fromEntity(Prompt prompt) {
        return new PromptUploadRequestDto(
                prompt.getPromptId(),
                prompt.getPromptContent(),
                PromptUploadRequestDto.ExampleType.TEXT, // 예시 타입에 맞게 지정
                prompt.getExampleContentUrl() // 예시 값 (필요시 prompt에서 가져오거나 빈 값)
        );
    }
}