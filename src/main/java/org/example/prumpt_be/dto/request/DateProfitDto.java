package org.example.prumpt_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
public class DateProfitDto {
    private LocalDateTime date;
    private int totalProfit;
}
