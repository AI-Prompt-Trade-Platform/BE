package org.example.prumpt_be.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionSummaryItemDto {
    private LocalDate summaryDate;
    private Integer soldCount;
    private BigDecimal totalRevenue;
    private LocalDateTime lastUpdated;
}