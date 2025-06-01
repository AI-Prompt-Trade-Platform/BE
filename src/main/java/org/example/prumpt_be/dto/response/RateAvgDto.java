package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.prumpt_be.domain.entity.PromptReviews;


// 사용자 평균 평점 DTO
@Getter
@Setter
public class RateAvgDto {
    private int userID;
    private double rateAvg;

    public RateAvgDto(int userID, double rateAvg) {
        this.userID = userID;
        this.rateAvg = rateAvg;
    }

}
