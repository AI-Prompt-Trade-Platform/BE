// src/main/java/org/example/prumpt_be/domain/entity/UserSalesSummaryId.java
package org.example.prumpt_be.dto.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

// UserSalesSummary 엔티티의 복합키를 위한 클래스
public class UserSalesSummaryId implements Serializable {
    private Integer userId;
    private LocalDate summaryDate;

    public UserSalesSummaryId(Integer userId, LocalDate summaryDate) {
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