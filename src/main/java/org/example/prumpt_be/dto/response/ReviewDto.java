package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReviewDto {
    private Long reviewID;
    private Integer rate;
    private String reviewContent;
    private BuyerDto buyer;    // 구매자(리뷰어) 정보
}
