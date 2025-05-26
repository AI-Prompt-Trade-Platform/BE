package org.example.prumpt_be.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.domain.entity.UserSalesSummary;
import org.example.prumpt_be.domain.entity.Users;
import org.example.prumpt_be.repository.PurchasesRepository;
import org.example.prumpt_be.repository.UserSalesSummaryRepository;
import org.example.prumpt_be.repository.UsersRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;


// 매일 자정, 전체 판매수익 정보를 집계, 저장하는 스케줄링 Service
@Service
@RequiredArgsConstructor
public class UserSalesSummaryScheduler {

    private final PurchasesRepository purchasesRepo;
    private final UserSalesSummaryRepository profitRepo;
    private final UsersRepository usersRepo;  // Users 엔티티 조회용

    /**
     * 매일 00:00 (Asia/Seoul) 에 실행
     * - 어제 날짜(yesterday)의 00:00~24:00 데이터 집계
     * - UserSalesSummary 테이블에 저장 또는 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void updateYesterdaySales() {
        // 1. 시간 범위 계산
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        LocalDateTime start  = yesterday.atStartOfDay();
        LocalDateTime end    = yesterday.plusDays(1).atStartOfDay();

        // 2. 모든 사용자 조회 (혹은 실제 판매자만 조회하도록 커스텀 메서드로 변경 가능)
        List<Users> allUsers = usersRepo.findAll();

        for (Users user : allUsers) {
            Integer userId = user.getUserID();

            // 3. 해당 사용자 어제치 집계 호출
            Object[] result = purchasesRepo.findDailyProfitBySeller(userId, start, end);

            // 4. null-safe 파싱
            long soldCount   = result[0] != null ? (Long) result[0] : 0L;
            BigDecimal total = result[1] != null
                    ? (BigDecimal) result[1]
                    : BigDecimal.ZERO;

            // 5. 사용자의 수익 요약 레코드가 있으면 로드, 없으면 새로 생성
            UserSalesSummary profit = profitRepo.findById(userId)
                    .orElseGet(() -> {
                        UserSalesSummary dto = new UserSalesSummary();
                        dto.setUser(user);
                        return dto;
                    });

            // 6. 값 세팅
            profit.setSoldCount((int) soldCount);
            profit.setTotalRevenue(total);
            profit.setSummaryDate(yesterday);
            profit.setLastUpdated(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

            // 7. 저장
            profitRepo.save(profit);
        }
    }
}
