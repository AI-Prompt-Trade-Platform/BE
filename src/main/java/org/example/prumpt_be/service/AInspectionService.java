package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.Prompts;
import org.example.prumpt_be.dto.request.PromptUploadRequestDto;
import org.example.prumpt_be.repository.PromptsRepository;
import org.springframework.stereotype.Service;

@Service
public class AInspectionService {

    private final S3Uploader s3Uploader; // S3에 파일 업로드 하는 클래스
    private final OpenAiService openAiService;
    private final PromptsRepository promptsRepository;

    public AInspectionService(S3Uploader s3Uploader , OpenAiService openAiService, PromptsRepository promptsRepository) {
        this.s3Uploader = s3Uploader;
        this.openAiService = openAiService;
        this.promptsRepository = promptsRepository;
    }

    // todo: 준호님 프롬프트 등록Service에 연동 필요(준호님이 S3에 업로드하는 로직이어야 함)
    public void handlePromptUploadAndEvaluation(PromptUploadRequestDto request) {
        // 1. S3에 파일 업로드
//        String exampleUrl = s3Uploader.upload(request.getExampleValue()); // S3Uploader를 사용하여 파일 업로드
          String exampleUrl = request.getExampleValue(); // 예시 URL은 프론트에서 받아온 값으로 대체


        // 2. 어떤 프롬프트인지 조회 (DB저장 이후)
        int promptId = request.getPromptId();
        Prompts prompt = promptsRepository.findById((long) promptId)
                .orElseThrow(() -> new IllegalArgumentException("Prompt not found with id: " + promptId));

        // 3. AI 평가 요청
        PromptUploadRequestDto targetPrompt = PromptUploadRequestDto.fromEntity(prompt);
        String inspectionResult = openAiService.getInspectionRate(makePromptForStock(targetPrompt));
        System.out.println(inspectionResult);

        // 4. 평가 결과 저장
        prompt.setAi_inspection_rate(inspectionResult);
        promptsRepository.save(prompt);
    }

    // 프롬프트 만드는 함수
    private String makePromptForStock(PromptUploadRequestDto prompt) {
        return switch (prompt.getExampleType()) {
            case TEXT -> makePromptForText(prompt);
            case IMAGE -> makePromptForImage(prompt);
            case VIDEO -> makePromptForVideo(prompt);
            default -> throw new IllegalArgumentException("지원하지 않는 ExampleType입니다: " + prompt.getExampleType());
        };
    }

    // 텍스트용 프롬프트
    private String makePromptForText(PromptUploadRequestDto prompt) {
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
                prompt.getPromptBody(),
                prompt.getExampleValue()
        );
    }

    // 이미지용 프롬프트
    private String makePromptForImage(PromptUploadRequestDto prompt) {
        return String.format(
                """
                        아래 기준에 따라 이미지 생성 프롬프트와 예시 결과물의 유사도를 평가해 [S, A, B, C, D] 등급 중 하나를 부여해줘.
                        
                        1. 프롬프트 내용: %s
                        2. 예시 결과물: %s
                        
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
                prompt.getPromptBody(),
                prompt.getExampleValue()
        );
    }

    // 영상용 프롬프트
    private String makePromptForVideo(PromptUploadRequestDto prompt) {
        return String.format(
                """
                        아래 기준에 따라 영상 생성 프롬프트와 예시 결과물의 유사도를 평가해 [S, A, B, C, D] 등급 중 하나를 부여해줘.
                        
                        1. 프롬프트 내용: %s
                        2. 예시 결과물: %s
                        
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
                prompt.getPromptBody(),
                prompt.getExampleValue()
        );
    }
}

