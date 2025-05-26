package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class EachDaysProfitDto {
    private LocalDate summaryDate;
    private BigDecimal totalRevenue;

    // 이 생성자가 JPQL의 new 표현식과 정확히 일치해야 합니다.
    public EachDaysProfitDto(LocalDate summaryDate, BigDecimal totalRevenue) {
        this.summaryDate   = summaryDate;
        this.totalRevenue  = totalRevenue;
    }

    public LocalDate SummaryDate() {
        return summaryDate;
    }

    public BigDecimal TotalRevenue() {
        return totalRevenue;
    }
}
