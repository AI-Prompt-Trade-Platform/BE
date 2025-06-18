package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.request.PromptUploadRequestDto;
import org.example.prumpt_be.repository.PromptsRepository;
import org.springframework.beans.factory.annotation.Value; // Value 어노테이션 추가
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class AInspectionService {

    private final S3Uploader s3Uploader;
    private final OpenAiService openAiService;
    private final PromptsRepository promptsRepository;

    // application.properties에서 각 버킷 이름 주입
    @Value("${aws.s3.bucket.text}")
    private String textBucketName;

    @Value("${aws.s3.bucket.image}")
    private String imageBucketName;

    @Value("${aws.s3.bucket.video}")
    private String videoBucketName;


    public AInspectionService(S3Uploader s3Uploader, OpenAiService openAiService, PromptsRepository promptsRepository) {
        this.s3Uploader = s3Uploader;
        this.openAiService = openAiService;
        this.promptsRepository = promptsRepository;
    }

    public void handlePromptUploadAndEvaluation(PromptUploadRequestDto request) {
        String exampleUrl = request.getExampleValue();
        MultipartFile exampleFile = request.getExampleFile();

        if (exampleFile != null && !exampleFile.isEmpty()) {
            try {
                String dirName; // S3 내에서의 폴더 구조 (예: "uploads", "public" 등 또는 타입별로 다르게)
                String targetBucketName;

                switch (request.getExampleType()) {
                    case TEXT:
                        dirName = "text_files"; // 예시 S3 디렉토리 이름
                        targetBucketName = textBucketName;
                        exampleUrl = s3Uploader.upload(exampleFile, targetBucketName, dirName);
                        break;
                    case IMAGE:
                        dirName = "image_files"; // 예시 S3 디렉토리 이름
                        targetBucketName = imageBucketName;
                        exampleUrl = s3Uploader.upload(exampleFile, targetBucketName, dirName);
                        break;
                    case VIDEO:
                        dirName = "video_files"; // 예시 S3 디렉토리 이름
                        targetBucketName = videoBucketName;
                        exampleUrl = s3Uploader.upload(exampleFile, targetBucketName, dirName);
                        break;
                    default:
                        // 기본 버킷을 사용하거나 예외 처리
                         throw new IllegalArgumentException("지원하지 않는 파일 타입 또는 파일 누락: " + request.getExampleType());
                        // 여기서는 기존 exampleUrl을 유지하거나, 혹은 공통 버킷을 사용할 수 있습니다.
                        // 예를 들어, 공통 버킷이 있다면:
                        // targetBucketName = commonBucketName; // @Value로 주입 필요
                        // dirName = "others";
                        // exampleUrl = s3Uploader.upload(exampleFile, targetBucketName, dirName);
//                        break; // 또는 예외 발생
                }
            } catch (IOException e) {
                throw new RuntimeException("S3 파일 업로드 중 오류 발생: " + e.getMessage(), e);
            }
        }

        Long promptId = request.getPromptId();
        Prompt prompt = promptsRepository.findById(promptId)
                .orElseThrow(() -> new IllegalArgumentException("Prompt not found with id: " + promptId));

        if (exampleFile != null && !exampleFile.isEmpty()) {
            // Prompt 엔티티의 exampleContentUrl 필드에 저장 (스키마에 따라 필드명 확인 필요)
            prompt.setExampleContentUrl(exampleUrl);
        }

        PromptUploadRequestDto targetPrompt = PromptUploadRequestDto.fromEntity(prompt);
        // fromEntity에서 exampleValue를 설정했다면 아래 라인은 필요 없을 수 있음
        // 또는 S3 업로드 후 URL로 항상 덮어쓰려면 유지
        targetPrompt.setExampleValue(exampleUrl);
        targetPrompt.setExampleType(request.getExampleType());


        String inspectionResult = openAiService.getInspectionRate(makePromptForStock(targetPrompt));
        System.out.println(inspectionResult);

        prompt.setAiInspectionRate(inspectionResult);
        promptsRepository.save(prompt);
    }

    // makePromptForStock 및 하위 메소드들은 변경 없음
    private String makePromptForStock(PromptUploadRequestDto promptDto) { // DTO를 그대로 사용
        return switch (promptDto.getExampleType()) {
            case TEXT -> makePromptForText(promptDto.getPromptBody(), promptDto.getExampleValue());
            case IMAGE -> makePromptForImage(promptDto.getPromptBody(), promptDto.getExampleValue());
            case VIDEO -> makePromptForVideo(promptDto.getPromptBody(), promptDto.getExampleValue());
            default -> throw new IllegalArgumentException("지원하지 않는 ExampleType입니다: " + promptDto.getExampleType());
        };
    }

    // 텍스트용 프롬프트
    private String makePromptForText(String promptBody, String exampleText) {
        return String.format(
                """
                        아래 기준에 따라 텍스트 생성 프롬프트와 예시 결과물의 유사도를 평가해 [S, A, B, C, D] 등급 중 하나를 부여해줘.
                        
                        1. 프롬프트 내용: %s
                        2. 예시 결과물: %s
                        
                        3. 평가 기준:
                         - **내용 일치도**: 프롬프트가 요구하는 주제, 정보, 메시지가 예시 결과물과 얼마나 일치하는지
                         - **형식/구조 일치도**: 요청된 텍스트 형태(글 길이, 문체, 구성 방식)가 예시와 얼마나 맞는지
                         - **톤앤매너**: 프롬프트에서 지시한 어조, 문체, 대상 독자에 맞는 표현이 구현되었는지
                         - **완성도**: 문법, 맞춤법, 논리적 흐름 등 텍스트의 전반적 품질
                        
                         - **등급 정의**
                           - S: 내용, 형식, 톤앤매너, 완성도 모든 면에서 프롬프트 요구사항과 거의 완벽하게 일치
                           - A: 대부분의 요구사항을 충족하나, 한두 가지 측면에서 매우 사소한 차이나 아쉬움
                           - B: 핵심 내용은 맞으나 형식/톤/완성도 중 일부에서 중간 정도의 차이나 부족함
                           - C: 기본 주제는 맞으나 여러 측면에서 프롬프트 의도와 상당한 차이
                           - D: 주제조차 맞지 않거나 텍스트 품질이 현저히 낮음
                        
                        4. 등급은 오직 하나만 선택.
                          반드시 아래 응답 포맷을 지켜줘.
                        
                        5. **응답 포맷:**
                          `[등급알파벳] (띄어쓰기) [아주 짧게 이 등급을 준 이유를 한 문장으로 설명]` 
                          예시: `A 내용과 형식은 완벽하나 톤이 약간 딱딱함`
                        
                        반드시 위의 포맷을 지켜 한 줄로만 답변해줘.
                        
                        """,
                promptBody,
                exampleText
        );
    }

    // 이미지용 프롬프트
    private String makePromptForImage(String promptBody, String exampleImageUrl) {
        return String.format(
                """
                        아래 기준에 따라 이미지 생성 프롬프트와 예시 결과물(URL)의 유사도를 평가해 [S, A, B, C, D] 등급 중 하나를 부여해줘.
                        
                        1. 프롬프트 내용: %s
                        2. 예시 결과물 URL: %s
                        
                        3. 평가 기준:
                          - **시각적 요소 일치도**: 프롬프트에서 요구한 객체, 인물, 배경, 색상이 예시 이미지에 얼마나 정확히 구현되었는지
                          - **구도/레이아웃 일치도**: 요청된 화면 구성, 앵글, 비율, 배치가 예시와 얼마나 일치하는지
                          - **스타일/분위기 일치도**: 지시한 예술 스타일, 무드, 톤, 질감이 예시 이미지에 얼마나 반영되었는지
                          - **디테일 완성도**: 세부 묘사, 해상도, 품질 등 이미지의 전반적 완성도
                          - **등급 정의**
                            - S: 시각적 요소, 구도, 스타일, 디테일 모든 면에서 프롬프트 요구사항과 거의 완벽하게 일치
                            - A: 대부분의 요구사항을 충족하나, 한두 가지 측면에서 매우 사소한 차이나 아쉬움
                            - B: 핵심 시각 요소는 맞으나 구도/스타일/디테일 중 일부에서 중간 정도의 차이나 부족함
                            - C: 기본 주제는 맞으나 여러 시각적 측면에서 프롬프트 의도와 상당한 차이
                            - D: 주요 객체나 테마조차 맞지 않거나 이미지 품질이 현저히 낮음
                        
                        4. 등급은 오직 하나만 선택.
                           반드시 아래 응답 포맷을 지켜줘.
                        
                        5. **응답 포맷:** 
                           `[등급알파벳] (띄어쓰기) [아주 짧게 이 등급을 준 이유를 한 문장으로 설명]` 
                           예시: `A 주요 객체와 색상은 완벽하나 구도가 약간 다름`
                        
                        반드시 위의 포맷을 지켜 한 줄로만 답변해줘.
                        """,
                promptBody,
                exampleImageUrl
        );
    }

    // 영상용 프롬프트
    private String makePromptForVideo(String promptBody, String exampleVideoUrl) {
        return String.format(
                """
                        아래 기준에 따라 영상 생성 프롬프트와 예시 결과물(URL)의 유사도를 평가해 [S, A, B, C, D] 등급 중 하나를 부여해줘.
                        
                        1. 프롬프트 내용: %s
                        2. 예시 결과물 URL: %s
                        
                        3. 평가 기준:
                          - **영상 내용 일치도**: 프롬프트에서 요구한 장면, 인물, 배경, 액션이 예시 영상에 얼마나 정확히 구현되었는지
                          - **시퀀스/스토리 일치도**: 요청된 영상 흐름, 장면 전환, 서사 구조가 예시와 얼마나 일치하는지
                          - **영상 스타일 일치도**: 지시한 촬영 기법, 시각적 톤, 색감, 화면 구성이 예시 영상에 얼마나 반영되었는지
                          - **기술적 완성도**: 영상 품질, 움직임 자연스러움, 연결성 등 영상의 전반적 완성도
                          - **등급 정의**
                            - S: 영상 내용, 시퀀스, 스타일, 기술적 완성도 모든 면에서 프롬프트 요구사항과 거의 완벽하게 일치
                            - A: 대부분의 요구사항을 충족하나, 한두 가지 측면에서 매우 사소한 차이나 아쉬움
                            - B: 핵심 내용은 맞으나 시퀀스/스타일/완성도 중 일부에서 중간 정도의 차이나 부족함
                            - C: 기본 장면은 맞으나 여러 측면에서 프롬프트 의도와 상당한 차이
                            - D: 주요 장면이나 액션조차 맞지 않거나 영상 품질이 현저히 낮음
                        
                        4. 등급은 오직 하나만 선택.
                           반드시 아래 응답 포맷을 지켜줘.
                        
                        5. **응답 포맷:**
                           `[등급알파벳] (띄어쓰기) [아주 짧게 이 등급을 준 이유를 한 문장으로 설명]` 
                           예시: `A 장면과 액션은 완벽하나 색감이 약간 다름`
                        
                        반드시 위의 포맷을 지켜 한 줄로만 답변해줘.
                        """,
                promptBody,
                exampleVideoUrl
        );
    }
}
