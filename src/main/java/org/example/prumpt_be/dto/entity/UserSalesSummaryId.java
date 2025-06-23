// 이 파일이 존재하지 않거나 내용이 다르다면, 아래 코드로 교체하거나 생성해주세요.
// src/main/java/org/example/prumpt_be/dto/entity/UserSalesSummaryId.java

package org.example.prumpt_be.dto.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable // 이 클래스가 다른 엔티티에 포함될 수 있음을 나타냅니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSalesSummaryId implements Serializable {

    // [수정됨] Integer -> Users 타입으로 변경하고, 관계를 매핑합니다.
    @ManyToOne
    @JoinColumn(name = "user_id") // 실제 DB의 컬럼명을 지정합니다.
    private Users userID;

    private LocalDate summaryDate;

    // 복합 키는 equals와 hashCode를 반드시 올바르게 오버라이드해야 합니다.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSalesSummaryId that = (UserSalesSummaryId) o;
        // Users 객체의 ID와 날짜를 비교합니다.
        return Objects.equals(userID.getUserId(), that.userID.getUserId()) &&
                Objects.equals(summaryDate, that.summaryDate);
    }

    @Override
    public int hashCode() {
        // Users 객체의 ID와 날짜로 해시코드를 생성합니다.
        return Objects.hash(userID.getUserId(), summaryDate);
    }
}


//package org.example.prumpt_be.dto.entity;
//
//import jakarta.persistence.Embeddable;
//import lombok.*;
//
//import java.io.Serializable;
//import java.time.LocalDate;
//import java.util.Objects;
//
//// 복합 키 클래스
//@Embeddable
//@Getter
//@Setter
//@NoArgsConstructor
//public class UserSalesSummaryId implements Serializable {
//    private Long userID;
//    private LocalDate summaryDate;
//
//    public UserSalesSummaryId(Long userId, LocalDate summaryDate) {
//        this.userID = userId;
//        this.summaryDate = summaryDate;
//    }
//
//    // equals, hashCode 필수
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof UserSalesSummaryId that)) return false;
//        return Objects.equals(userID, that.userID) &&
//               Objects.equals(summaryDate, that.summaryDate);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(userID, summaryDate);
//    }
//}