package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.response.PromptAvgRateDto;
import org.example.prumpt_be.repository.PromptReviewsRepository;
import org.example.prumpt_be.service.MoniteringService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {
    private final PromptReviewsRepository promptReviewsRepository;
    private final MoniteringService moniteringService;

    public MonitoringController(PromptReviewsRepository promptReviewsRepository, MoniteringService moniteringService) {
        this.promptReviewsRepository = promptReviewsRepository;
        this.moniteringService = moniteringService;
    }

    // 인증된 사용자가 볼 수 있는 프롬프트 목록 조회 예시
    @GetMapping("/prompts")
    public List<PromptAvgRateDto> listPrompts(
            @AuthenticationPrincipal Jwt jwt
    ) {

        // 1) JWT에서 sub(사용자 ID) 뽑아오기
        String userAuth0Id = jwt.getSubject();
        int userId = moniteringService.getUserIdByAuth0Id(userAuth0Id);
//        String userAuth0Id = jwt.getSubject();

        // 2) 서비스 호출: 해당 사용자의 평균 별점 조회 (Service계층으로 이동)
        List<PromptAvgRateDto> eachPromptsRate = moniteringService.getAvgRatesByUser(userId);

        // 3) 결과 반환
        return eachPromptsRate;  // 그대로 반환

    }
}
