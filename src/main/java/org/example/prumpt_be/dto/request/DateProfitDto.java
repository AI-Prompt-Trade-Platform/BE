package org.example.prumpt_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.prumpt_be.domain.entity.Users;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
public class DateProfitDto {
    private Users userId;
    private LocalDateTime date;
    private int totalProfit;
}
