package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.prumpt_be.dto.entity.Users;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EachDaysProfitDto {
    private int userId;
    private LocalDate summaryDate;
    private BigDecimal totalRevenue;


    public LocalDate SummaryDate() {
        return summaryDate;
    }

    public BigDecimal TotalRevenue() {
        return totalRevenue;
    }
}
