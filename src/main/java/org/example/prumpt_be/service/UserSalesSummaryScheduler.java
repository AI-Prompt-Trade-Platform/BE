package org.example.prumpt_be.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.UserSalesSummary;
import org.example.prumpt_be.dto.entity.UserSalesSummaryId;
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.response.DailySalesDto;
import org.example.prumpt_be.repository.PurchasesRepository;
import org.example.prumpt_be.repository.UserSalesSummaryRepository;
import org.example.prumpt_be.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSalesSummaryScheduler {

    private static final Logger log = LoggerFactory.getLogger(UserSalesSummaryScheduler.class);
    private final PurchasesRepository purchasesRepo;
    private final UserSalesSummaryRepository profitRepo;
    private final UsersRepository usersRepo;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    @Transactional
    public void updateYesterdaySales() {
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        LocalDateTime start  = yesterday.atStartOfDay();
        LocalDateTime end    = yesterday.plusDays(1).atStartOfDay();

        List<Users> allUsers = usersRepo.findAll();
        log.info("어제({}) 날짜의 판매 요약 업데이트를 시작합니다. 대상 유저: {}명", yesterday, allUsers.size());

        for (Users user : allUsers) {
            try {
                DailySalesDto salesData = purchasesRepo.findDailySalesBySeller(user, start, end);

                long soldCount = (salesData != null) ? salesData.soldCount() : 0L;
                BigDecimal totalRevenue = (salesData != null && salesData.totalRevenue() != null) ? salesData.totalRevenue() : BigDecimal.ZERO;

                // 1. 복합 키(ID) 객체를 먼저 생성합니다.
                UserSalesSummaryId summaryId = new UserSalesSummaryId(user, yesterday);

                // 2. JpaRepository의 표준 findById() 메서드를 사용하여 엔티티를 조회합니다.
                UserSalesSummary profit = profitRepo.findById(summaryId)
                        .orElseGet(() -> {
                            // 엔티티가 없으면 새로 생성합니다. ID는 이미 만들어진 summaryId를 사용합니다.
                            log.info("유저 ID: {}, 날짜: {}에 대한 새 판매 요약 레코드를 생성합니다.", user.getUserId(), yesterday);
                            return UserSalesSummary.builder()
                                    .id(summaryId)
                                    .soldCount(0)
                                    .totalRevenue(BigDecimal.ZERO)
                                    .build();
                        });

                // 3. 조회된 데이터로 엔티티의 값을 업데이트합니다.
                profit.setSoldCount((int) soldCount);
                profit.setTotalRevenue(totalRevenue);
                profit.setLastUpdated(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

                profitRepo.save(profit);

            } catch (Exception e) {
                // 개별 유저 처리 중 오류가 발생해도 전체 스케줄링이 멈추지 않도록 처리합니다.
                log.error("유저 ID: {}의 판매 요약 업데이트 중 오류 발생", user.getUserId(), e);
            }
        }
        log.info("판매 요약 업데이트를 완료했습니다.");
    }
}