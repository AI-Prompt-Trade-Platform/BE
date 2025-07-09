package org.example.prumpt_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.prumpt_be.dto.entity.Prompt;
import org.springframework.web.multipart.MultipartFile;

// 평가 요청 DTO
@Getter
@Setter
@AllArgsConstructor
public class PromptUploadRequestDto {
    private Long promptId; // 프롬프트 ID
    private String promptBody;
    private ExampleType exampleType;  // IMAGE, VIDEO, TEXT (enum)
    private MultipartFile exampleFile; // 이미지, 영상, 또는 텍스트 파일을 받을 단일 필드
    private String exampleValue; // S3 URL 또는 텍스트 예시 내용 (파일이 없을 경우)

    public enum ExampleType {
        IMAGE, VIDEO, TEXT
    }

    // exampleFile 필드를 직접 반환합니다.
    // 실제 파일 처리는 exampleType에 따라 서비스 레이어에서 이루어집니다.
    // public MultipartFile getExampleFile() {
    //     return this.exampleFile;
    // }

    // Prompt 엔티티로부터 DTO를 생성하는 정적 팩토리 메소드
    public static PromptUploadRequestDto fromEntity(Prompt prompt) {
        if (prompt == null) {
            return null;
        }

        ExampleType type = ExampleType.TEXT; // 기본값 또는 prompt에서 가져온 타입
        String exampleContent = prompt.getExampleContentUrl();

        if (exampleContent != null) {
            if (exampleContent.matches("(?i).*\\.(jpeg|jpg|png|gif)$")) {
                type = ExampleType.IMAGE;
            } else if (exampleContent.matches("(?i).*\\.(mp4|mov|avi|wmv)$")) {
                type = ExampleType.VIDEO;
            }
        }

        return new PromptUploadRequestDto(
                prompt.getPromptId(),
                prompt.getPromptContent(),
                type,
                null, // 엔티티에서 DTO 변환 시 파일은 null
                exampleContent
        );
    }

    // 기본 생성자
    public PromptUploadRequestDto() {
    }
}