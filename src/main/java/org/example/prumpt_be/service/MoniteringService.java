package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.Users; // Users 임포트
import org.example.prumpt_be.dto.response.*;
import org.example.prumpt_be.repository.PromptReviewsRepository;
import org.example.prumpt_be.repository.PromptsRepository;
import org.example.prumpt_be.repository.UserSalesSummaryRepository;
import org.example.prumpt_be.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 읽기 전용 트랜잭션 사용

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 모든 조회 메소드에 대해 클래스 레벨에서 읽기 전용 트랜잭션 적용
public class MoniteringService {

    private final UserSalesSummaryRepository userSalesSummaryRepository;
    private final PromptReviewsRepository promptReviewsRepository;
    private final UsersRepository usersRepository;
    private final PromptsRepository promptRepository;

    public enum PeriodType {
        MONTH, HALF_YEAR, YEAR
    }

    // 이 헬퍼 메소드는 더 이상 필요 없으므로 제거합니다.
    // public Integer getUserIdByAuth0Id(String auth0_id) { ... }

    // 사용자 조회를 위한 헬퍼 메소드 (코드 중복 감소)
    private Users findUserByAuth0Id(String auth0Id) {
        return usersRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id));
    }

    // [수정됨] auth0Id를 받아 Users 객체로 쿼리합니다.
    public List<EachDaysProfitDto> findDailyRevenueWithZero(String auth0Id, PeriodType periodType) {
        Users user = findUserByAuth0Id(auth0Id);
        LocalDate end = LocalDate.now();
        LocalDate start = switch (periodType) {
            case MONTH -> end.minusMonths(1).plusDays(1);
            case HALF_YEAR -> end.minusMonths(6).plusDays(1);
            case YEAR -> end.minusYears(1).plusDays(1);
        };

        // UserSalesSummaryRepository가 Users 객체를 받도록 리팩토링되었다고 가정
        List<EachDaysProfitDto> dbResult = userSalesSummaryRepository.findDailyRevenueByUserAndPeriod(user, start, end);
        Map<LocalDate, BigDecimal> revenueMap = new HashMap<>();
        for (EachDaysProfitDto dto : dbResult) {
            revenueMap.put(dto.getSummaryDate(), dto.getTotalRevenue());
        }

        List<EachDaysProfitDto> result = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            BigDecimal revenue = revenueMap.getOrDefault(date, BigDecimal.ZERO);
            result.add(new EachDaysProfitDto(user.getUserId(), date, revenue));
        }
        return result;
    }

    // [수정됨]
    public List<PromptAvgRateDto> findAvgRateOfAllPromptsByUserId(String auth0Id) {
        Users user = findUserByAuth0Id(auth0Id);
        // promptReviewsRepository도 Users 객체를 받도록 리팩토링되었다고 가정
        return promptReviewsRepository.findAvgRateByPromptOfUser(user);
    }

    // [수정됨]
    public RateAvgDto getAvgRateOfAllPromptsByUserId(String auth0Id) {
        Users user = findUserByAuth0Id(auth0Id);
        // promptReviewsRepository도 리팩토링되었다고 가정
        RateAvgDto raw = promptReviewsRepository.findAvgRateOfAllPromptsByUserId(user);
        if (raw == null) return null;
        Double avgRate = raw.getRateAvg();
        if (avgRate == null) return raw;
        double rounded = Math.round(avgRate * 10) / 10.0;
        return new RateAvgDto(raw.getUserID(), rounded);
    }

    // [수정됨]
    public BigDecimal getProfitOfThisMonthByUserId(String auth0Id) {
        Users user = findUserByAuth0Id(auth0Id);
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now();
        return userSalesSummaryRepository.findTotalRevenueByUserIdAndPeriod(user, startOfMonth, endOfMonth);
    }

    // [수정됨]
    public Long getTotalSalesCountByUserId(String auth0Id) {
        Users user = findUserByAuth0Id(auth0Id);
        return userSalesSummaryRepository.countTotalSalesByUserId(user);
    }

    // [수정됨]
    public List<PromptDto> getAllPromptsByOwnerId(String auth0Id) {
        Users user = findUserByAuth0Id(auth0Id);
        // promptRepository도 리팩토링되었다고 가정
        return promptRepository.findAllByOwnerId(user);
    }
}