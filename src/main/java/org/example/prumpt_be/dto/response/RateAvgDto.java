package org.example.prumpt_be.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RateAvgDto {
    private int userID;
    private double rateAvg;
}
