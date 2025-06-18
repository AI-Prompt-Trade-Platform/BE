package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoniteringResponseDto {
    private List<PromptDto> prompts = new ArrayList<>();
    private List<EachDaysProfitDto> dailyProfit = new ArrayList<>();
    private RateAvgDto avgRate;
    private BigDecimal thisMonthProfit;
    private Long totalSalesCount;
}

//            todo: 프롬프트를 이런 형태로 반환하는게 좋을거같음 (PromptDto -> PromptSummaryDto 로 변경)
//            id: 1,
//            title: "창의적 글쓰기 마스터",
//            description: "소설, 에세이, 시나리오 작성을 위한 전문 프롬프트",
//            category: "글쓰기",
//            rating: 4.8,
//            price: 15000,
//            author: "김작가",
//            downloads: 1250,
//            tags: ["창작", "소설", "시나리오"]