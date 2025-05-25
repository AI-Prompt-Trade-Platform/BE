package org.example.prumpt_be.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "UserSalesSummary")
public class PromptSales {
    @Id
    private LocalDateTime saleDate;
    @Column(nullable = false, unique = true)
    private int userID;
    @Column
    private int salesCount;
    @Column
    private int totalProfit;
}
