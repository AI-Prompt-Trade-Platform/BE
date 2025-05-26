package org.example.prumpt_be.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "user_sales_summary")
public class UserSalesSummary {

    // 1:1 매핑을 위해 userId를 PK로 사용
    @Id
    @Column(name = "user_id")
    private Integer userId;

    // User 엔티티와 1:1 매핑
    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private Users user;

    // 당일 판매 건수
    @Column(name = "sold_count", nullable = false)
    private Integer soldCount;

    // 당일 총 매출
    @Column(name = "total_revenue", nullable = false)
    private BigDecimal totalRevenue;

    // 집계 대상 날짜 (ex. “2025-05-25”)
    @Column(name = "summary_date", nullable = false)
    private LocalDate summaryDate;

    // 마지막 업데이트 시각
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

}
