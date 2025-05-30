package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.domain.entity.PromptReviews;


// 사용자 평균 평점 DTO
@Getter
@AllArgsConstructor
public class RateAvgDto {
    private String userID;
    private double rateAvg;
}
