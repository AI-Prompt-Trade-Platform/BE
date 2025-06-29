// /Users/madecafe/workSpace/Prumpt_2nd_Prj/Prumpt_BE/src/main/java/org/example/prumpt_be/service/OpenAiService.java
package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.request.ChatCompletionRequestDto;
import org.example.prumpt_be.dto.request.ChatCompletionRequestDto.ImageUrl;
import org.example.prumpt_be.dto.request.ChatCompletionRequestDto.Message;
import org.example.prumpt_be.dto.request.ChatCompletionRequestDto.MessageContent;
import org.example.prumpt_be.dto.response.ChatCompletionResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final WebClient openAiWebClient;

    public String getInspectionRate(String prompt) {
        // 요청 객체 생성
        ChatCompletionRequestDto request = new ChatCompletionRequestDto();
        request.setModel("gpt-4o");
        // ChatCompletionRequestDto의 내부 클래스이므로 명시적으로 접근
        request.setMessages(List.of(new Message("user", prompt)));

        // OpenAI API에 POST 요청
        ChatCompletionResponseDto response = openAiWebClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatCompletionResponseDto.class)
                .block(); // block()을 사용하여 동기적으로 응답을 기다립니다.

        // 응답에서 텍스트만 추출해서 리턴
        if (response != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }
        return "AI로부터 응답을 받지 못했습니다.";
    }

    /**
     * 이미지 URL(Pre-signed URL)과 프롬프트 본문을 받아 AI 평가를 요청합니다.
     * @param promptBody 사용자가 작성한 프롬프트 내용
     * @param imageUrl S3 Pre-signed URL
     * @return AI의 평가 결과
     */
    public String getInspectionRateForImageUrl(String promptBody, String imageUrl) {
        // AI에게 전달할 지시문 생성
        String instructionText = createInstructionTextForImage(promptBody);

        // OpenAI Vision API가 요구하는 형식으로 요청 본문 구성
        Message message = new Message("user", List.of(
                MessageContent.ofText(instructionText),
                MessageContent.ofImageUrl(new ImageUrl(imageUrl)) // URL을 올바른 객체에 담아 전달
        ));

        ChatCompletionRequestDto request = new ChatCompletionRequestDto();
        request.setModel("gpt-4o"); // Vision 기능이 있는 모델 사용
        request.setMessages(List.of(message));

        // OpenAI API에 POST 요청
        ChatCompletionResponseDto response = openAiWebClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatCompletionResponseDto.class)
                .block();

        if (response != null && !response.getChoices().isEmpty()) {
            return response.getChoices().get(0).getMessage().getContent();
        }
        return "AI로부터 응답을 받지 못했습니다.";
    }

    private String createInstructionTextForImage(String promptBody) {
        // AI 지시문 템플릿
        return String.format(
                """
                        너는 프롬프트를 읽고, 프롬프트의 내용이 함께 제공된 예시 결과물을 만들어 낼 수 있는지 정확도를 평가하는 "프롬프트 엔지니어"야.
                        아래 기준에 따라 이미지 생성 프롬프트와 예시 결과물의 유사도를 평가해 [S, A, B, C, D] 등급 중 하나를 부여해줘.
                        
                        1. 프롬프트 내용: %s
                        
                        2. 평가 기준:
                          - 시각적 요소 일치도: 프롬프트에서 요구한 객체, 인물, 배경, 색상이 예시 이미지에 얼마나 정확히 구현되었는지
                          - 구도/레이아웃 일치도: 요청된 화면 구성, 앵글, 비율, 배치가 예시와 얼마나 일치하는지
                          - 스타일/분위기 일치도: 지시한 예술 스타일, 무드, 톤, 질감이 예시 이미지에 얼마나 반영되었는지
                          - 디테일 완성도: 세부 묘사, 해상도, 품질 등 이미지의 전반적 완성도
                        
                        3. 등급은 오직 하나만 선택.
                           반드시 아래 응답 포맷을 지켜줘.
                           만약 평가할 수가 없는 상황일떄에는 등급을 X 로 하고, 이유를 짧게 "~함" 같은 말투로 끝내줘


                        4. 응답 포맷:
                           `[등급알파벳] (띄어쓰기) [아주 짧게 이 등급을 준 이유를 한 문장으로 설명]`
                           예시: `A 주요 객체와 색상은 완벽하나 구도가 약간 다름`

                        반드시 위의 포맷을 지켜 한 줄로만 답변해줘.
                        """,
                promptBody
        );
    }
}