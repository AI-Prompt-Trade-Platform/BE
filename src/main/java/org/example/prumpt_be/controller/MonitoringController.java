package org.example.prumpt_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.prumpt_be.dto.response.*;
import org.example.prumpt_be.service.MoniteringService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {
    private final MoniteringService moniteringService;

    //생성자
    public MonitoringController( MoniteringService moniteringService) {
        this.moniteringService = moniteringService;
    }

    // 인증된 사용자의 수익요약 페이지 조회
    @Operation(summary = "유저의 거래요약 페이지", description = "유저의 토큰을 받아 거래요약 페이지를 조회합니다.")
    @GetMapping("/prompts")
    public MoniteringResponseDto listPrompts(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "period", required = false, defaultValue = "MONTH") MoniteringService.PeriodType period) {

        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        int userId = moniteringService.getUserIdByAuth0Id(userAuth0Id);

        //1. 판매중인 프롬프트 리스트 조회
        List<PromptDto> allPrompts = moniteringService.getAllPromptsByOwnerId(userId);
        //2. 특정 기간(1달,6개월,1년 중 택1) 동안의 일일 수익 조회(FE에서 차트 그리기용)
        List<EachDaysProfitDto> eachDaysProfits = moniteringService.findDailyRevenueWithZero(userId, period);
        //3. 유저의 별점 평균 조회
        RateAvgDto avgRate = moniteringService.getAvgRateOfAllPromptsByUserId(userId);
        //4. 유저의 이달 총 수입 조회
        BigDecimal thisMonthProfit = moniteringService.getProfitOfThisMonthByUserId(userId);
        //5. 총 판매 건수
        Long totalSalesCount = moniteringService.getTotalSalesCountByUserId(userId);

        return new MoniteringResponseDto(
                allPrompts,                // ① 판매 중인 프롬프트 전체 리스트
                eachDaysProfits,           // ③ 특정 기간 동안의 일일 수익
                avgRate,                   // ④ 전체 평균 별점
                thisMonthProfit,           // ⑤ 이달 총 수익
                totalSalesCount            // ⑥ 총 판매 수
        );

    }

}
