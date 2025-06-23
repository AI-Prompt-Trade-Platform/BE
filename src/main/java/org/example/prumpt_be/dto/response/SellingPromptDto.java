package org.example.prumpt_be.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class SellingPromptDto {
    private Long promptId;
    private String promptName;
    private Integer price;
    private String aiInspectionRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long totalSalesCount; // 총 판매 건수 (집계 필요)
    private BigDecimal totalRevenue; // 총 판매 수익 (집계 필요)
}