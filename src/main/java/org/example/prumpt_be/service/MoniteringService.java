package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.response.EachDaysProfitDto;
import org.example.prumpt_be.repository.UserSalesSummaryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MoniteringService {
    //todo: Service 테스트 작성하기

    private final UserSalesSummaryRepository userSalesSummaryRepository;

    public enum PeriodType {
        MONTH, HALF_YEAR, YEAR
    }


    //특정 기간(1달,6갸월,1년 중 택1) 동안의 일일 수익 조회(FE에서 차트 그리기용)
    public List<EachDaysProfitDto> findDailyRevenueWithZero(Integer userId, PeriodType periodType) {
        LocalDate end = LocalDate.now();
        LocalDate start;
        switch (periodType) {
            case MONTH:
                start = end.minusMonths(1).plusDays(1);
                break;
            case HALF_YEAR:
                start = end.minusMonths(6).plusDays(1);
                break;
            case YEAR:
                start = end.minusYears(1).plusDays(1);
                break;
            default:
                throw new IllegalArgumentException("지원하지 않는 기간 범위입니다.");
        }

        List<EachDaysProfitDto> dbResult = userSalesSummaryRepository.findDailyRevenueByUserAndPeriod(userId, start, end);
        Map<LocalDate, BigDecimal> revenueMap = new HashMap<>();
        for (EachDaysProfitDto dto : dbResult) {
            revenueMap.put(dto.getSummaryDate(), dto.getTotalRevenue());
        }

        List<EachDaysProfitDto> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            BigDecimal revenue = revenueMap.getOrDefault(date, BigDecimal.ZERO);
            result.add(new EachDaysProfitDto(date, revenue));
        }
        return result;
    }
}
