package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.response.*;
import org.example.prumpt_be.repository.PromptReviewsRepository;
import org.example.prumpt_be.repository.PromptsRepository;
import org.example.prumpt_be.repository.UserSalesSummaryRepository;
import org.example.prumpt_be.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//todo: 수익 모니터링 서비스 (필수)
@Service
@RequiredArgsConstructor
public class MoniteringService {

    private final UserSalesSummaryRepository userSalesSummaryRepository;
    private final PromptReviewsRepository promptReviewsRepository;
    private final UsersRepository usersRepository;
    private final PromptsRepository promptRepository;

    public enum PeriodType {
        MONTH, HALF_YEAR, YEAR
    }

    // Auth0 ID(sub)로 사용자 조회 (Auth0ID => UserId 로 변경 용도)
    public int getUserIdByAuth0Id(String auth0_id) {
        return usersRepository.findByAuth0Id(auth0_id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."))
                .getUserId();
    }

    //특정 기간(1달,6개월,1년 중 택1) 동안의 일일 수익 조회(FE에서 차트 그리기용)
    public List<EachDaysProfitDto> findDailyRevenueWithZero(int userId, PeriodType periodType) {
        LocalDate end = LocalDate.now();
        LocalDate start = switch (periodType) {
            case MONTH -> end.minusMonths(1).plusDays(1);
            case HALF_YEAR -> end.minusMonths(6).plusDays(1);
            case YEAR -> end.minusYears(1).plusDays(1);
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


    // 유저의 프롬프트별 별점 평균 조회
    public List<PromptAvgRateDto> findAvgRateOfAllPromptsByUserId(int userId) {
        return promptReviewsRepository.findAvgRateByPromptOfUser(userId);
    }

    // 유저의 별점 평균 조회
    public RateAvgDto getAvgRateOfAllPromptsByUserId(int userId) {
        RateAvgDto raw = promptReviewsRepository.findAvgRateOfAllPromptsByUserId(userId);
        if (raw == null) return null;
        // 평균 별점이 Double 타입이라고 가정
        Double avgRate = raw.getRateAvg();
        if (avgRate == null) return raw;
        // 소수점 한자리로 반올림
        double rounded = Math.round(avgRate * 10) / 10.0;
        // 새로운 RateAvgDto로 반환 (생성자에 맞게 수정)
        return new RateAvgDto(raw.getUserID(), rounded);
    }

    // 유저의 이달 총 수입 금액 조회
    public BigDecimal getProfitOfThisMonthByUserId(int userId) {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        return userSalesSummaryRepository.findTotalRevenueByUserIdAndPeriod(userId, startOfMonth, endOfMonth);
    }

    // 유저의 총 판매 수 조회
    public Long getTotalSalesCountByUserId(int userId) {
        return userSalesSummaryRepository.countTotalSalesByUserId(userId);
    }

    // 판매중인 프롬프트 조회
    public List<PromptDto> getAllPromptsByOwnerId(int userId) {
        // 판매중인 프롬프트 리스트 조회
        return promptRepository.findAllByOwnerId(userId);
    }

}
