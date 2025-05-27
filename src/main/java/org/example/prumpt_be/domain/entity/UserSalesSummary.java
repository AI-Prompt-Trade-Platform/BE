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
@IdClass(UserSalesSummaryId.class) // 추가
@Table(name = "user_sales_summary")
public class UserSalesSummary {

    // 1:1 매핑을 위해 userId를 PK로 사용
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Id
    @Column(name = "summary_date")
    private LocalDate summaryDate;

    @OneToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "sold_count", nullable = false)
    private Integer soldCount;

    @Column(name = "total_revenue", nullable = false)
    private BigDecimal totalRevenue;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

}
