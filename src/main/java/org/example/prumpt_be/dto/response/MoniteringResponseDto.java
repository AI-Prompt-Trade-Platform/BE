package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.prumpt_be.domain.entity.Prompts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoniteringResponseDto {
    // 기존 List<Prompts> 대신 DTO를 담도록 수정
    private List<PromptDto> prompts = new ArrayList<>();
    private List<EachDaysProfitDto> dailyProfit = new ArrayList<>();
    private RateAvgDto avgRate;
    private BigDecimal thisMonthProfit;
    private Long totalSalesCount;
}