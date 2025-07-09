// /Users/madecafe/workSpace/Prumpt_2nd_Prj/Prumpt_BE/src/main/java/org/example/prumpt_be/dto/response/DailySalesDto.java

package org.example.prumpt_be.dto.response;

import java.math.BigDecimal;

public record DailySalesDto(long soldCount, BigDecimal totalRevenue) {
    /**
     * JPQL의 집계 함수(COUNT, SUM)가 null을 반환할 경우를 대비한 생성자
     */
    public DailySalesDto(Long soldCount, BigDecimal totalRevenue) {
        this(
                soldCount != null ? soldCount : 0L,
                totalRevenue != null ? totalRevenue : BigDecimal.ZERO
        );
    }

    /**
     * [추가된 생성자]
     * JPQL의 `COUNT(Long)`와 `SUM(Long)` 결과 타입을 직접 받기 위한 생성자입니다.
     * Long 타입의 수익을 내부에서 BigDecimal로 변환합니다.
     */
    public DailySalesDto(Long soldCount, Long totalRevenue) {
        this(
                soldCount != null ? soldCount : 0L,
                totalRevenue != null ? BigDecimal.valueOf(totalRevenue) : BigDecimal.ZERO
        );
    }
}