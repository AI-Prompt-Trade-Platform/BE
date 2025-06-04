package org.example.prumpt_be.dto.response;

import lombok.Getter;
import lombok.Setter;


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
