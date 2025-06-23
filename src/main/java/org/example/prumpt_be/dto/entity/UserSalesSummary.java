// 이 파일의 내용이 다르다면, 아래 코드를 참고하여 수정해주세요.
// src/main/java/org/example/prumpt_be/dto/entity/UserSalesSummary.java

package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSalesSummary {

    // [수정됨] 복합 키를 사용하도록 @EmbeddedId를 지정합니다.
    @EmbeddedId
    private UserSalesSummaryId id;

    private int soldCount;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalRevenue;

    private LocalDateTime lastUpdated;
}


//package org.example.prumpt_be.dto.entity;
//
//import lombok.NoArgsConstructor;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.UpdateTimestamp;
//import java.io.Serializable;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "user_sales_summary", indexes = {
//        @Index(name = "idx_summary_summary_date", columnList = "summary_date")
//})
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//@IdClass(UserSalesSummaryId.class)
//public class UserSalesSummary {
//
//    @Id
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
//    private Users userID;
//
//    @Id
//    @Column(name = "summary_date", nullable = false)
//    private LocalDate summaryDate;
//
//    @Column(name = "sold_count", nullable = false)
//    private Integer soldCount;
//
//    @Column(name = "total_revenue", nullable = false, precision = 19, scale = 2)
//    private BigDecimal totalRevenue;
//
//    @UpdateTimestamp // 생성 및 수정 시 자동으로 시간 기록 (스키마는 last_updated)
//    @Column(name = "last_updated", nullable = false)
//    private LocalDateTime lastUpdated;
//}