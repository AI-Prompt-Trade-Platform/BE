package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.response.EachDaysProfitDto;
import org.example.prumpt_be.dto.response.PromptAvgRateDto;
import org.example.prumpt_be.repository.PromptReviewsRepository;
import org.example.prumpt_be.repository.UserSalesSummaryRepository;
import org.example.prumpt_be.repository.UsersRepository;
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
    private final PromptReviewsRepository promptReviewsRepository;
    private final UsersRepository usersRepository;

    public enum PeriodType {
        MONTH, HALF_YEAR, YEAR
    }


    //특정 기간(1달,6갸월,1년 중 택1) 동안의 일일 수익 조회(FE에서 차트 그리기용)
    public List<EachDaysProfitDto> findDailyRevenueWithZero(int userId, PeriodType periodType) {
        LocalDate end = LocalDate.now();
        LocalDate start = switch (periodType) {
            case MONTH -> end.minusMonths(1).plusDays(1);
            case HALF_YEAR -> end.minusMonths(6).plusDays(1);
            case YEAR -> end.minusYears(1).plusDays(1);
            default -> throw new IllegalArgumentException("지원하지 않는 기간 범위입니다.");
        };

        // DB에서 해당 기간 동안의 일일 수익 조회
        List<EachDaysProfitDto> dbResult = userSalesSummaryRepository.findDailyRevenueByUserAndPeriod(userId, start, end);
        Map<LocalDate, BigDecimal> revenueMap = new HashMap<>();
        for (EachDaysProfitDto dto : dbResult) {
            revenueMap.put(dto.getSummaryDate(), dto.getTotalRevenue());
        }

        List<EachDaysProfitDto> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            BigDecimal revenue = revenueMap.getOrDefault(date, BigDecimal.ZERO);
            result.add(new EachDaysProfitDto(userId, date, revenue));
        }
        return result;
    }


    //특정 유저의 프롬프트별 전체 별점 평균 조회
    public List<PromptAvgRateDto> getAvgRatesByUser(int userId) {
        return promptReviewsRepository.findAvgRateByPromptOfUser(userId);
    }

    // Auth0 ID(sub)로 사용자 조회 (Auth0ID => UserId 로 변경 용도)
    public int getUserIdByAuth0Id(String auth0Id) {
        return usersRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."))
                .getUserID();
    }
}
