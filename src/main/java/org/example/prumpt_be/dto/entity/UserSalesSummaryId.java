package org.example.prumpt_be.dto.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// 복합 키 클래스
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserSalesSummaryId implements Serializable {
    private Long userId;
    private LocalDate summaryDate;

    public UserSalesSummaryId(Long userId, LocalDate summaryDate) {
        this.userId = userId;
        this.summaryDate = summaryDate;
    }

    // equals, hashCode 필수
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSalesSummaryId that)) return false;
        return Objects.equals(userId, that.userId) &&
               Objects.equals(summaryDate, that.summaryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, summaryDate);
    }
}