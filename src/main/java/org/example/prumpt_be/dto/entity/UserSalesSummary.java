package org.example.prumpt_be.dto.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_sales_summary")
public class UserSalesSummary {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "summary_date")
    private LocalDate summaryDate;

    @Column(name = "sold_count")
    private Integer soldCount = 0;

    @Column(name = "total_revenue")
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    public UserSalesSummary(Integer userId, LocalDate summaryDate) {
        this.userId = userId;
        this.summaryDate = summaryDate;
        this.soldCount = 0;
        this.lastUpdated = LocalDateTime.now();
    }
}