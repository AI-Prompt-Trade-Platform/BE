package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.request.ChatCompletionRequestDto;
import org.example.prumpt_be.dto.response.ChatCompletionResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

//todo: OpenAI API를 호출하는 서비스 (필수)
@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final WebClient openAiWebClient;

    public String getInspectionRate(String prompt) {
        // 요청 객체 만들어서
        ChatCompletionRequestDto request = new ChatCompletionRequestDto();
        request.setModel("gpt-4o");
        request.setMessages(List.of(new ChatCompletionRequestDto.Message("user", prompt)));

        // OpenAI API에 POST 요청
        ChatCompletionResponseDto response = openAiWebClient.post()
                .uri("/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatCompletionResponseDto.class)
                .block(); // block()이 붙으면 '동기적으로' 응답을 기다려!

        // 응답에서 텍스트만 추출해서 리턴
        return response.getChoices().get(0).getMessage().getContent();}
}
