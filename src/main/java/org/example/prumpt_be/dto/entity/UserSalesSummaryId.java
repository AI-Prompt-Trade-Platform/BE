package org.example.prumpt_be.dto.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

// 복합 키 클래스
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserSalesSummaryId implements Serializable {
    private Long user; // User 엔티티의 ID 타입과 일치
    private LocalDate summaryDate;
}
