package org.example.prumpt_be.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.entity.UserSalesSummary;
import org.example.prumpt_be.dto.entity.UserSalesSummaryId; // ID 클래스 임포트
import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.response.DailySalesDto;
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

@Service
@RequiredArgsConstructor
public class UserSalesSummaryScheduler {

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

        for (Users user : allUsers) {
            DailySalesDto salesData = purchasesRepo.findDailySalesBySeller(user, start, end);

            long soldCount   = salesData.soldCount();
            BigDecimal total = salesData.totalRevenue();

            // [수정됨] 복합 키를 사용하여 엔티티를 올바르게 생성합니다.
            UserSalesSummary profit = profitRepo.findById_UserIDAndId_SummaryDate(user, yesterday)
                    .orElseGet(() -> {
                        // 1. 복합 키(ID) 객체를 먼저 생성합니다.
                        UserSalesSummaryId newId = new UserSalesSummaryId(user, yesterday);

                        // 2. 생성된 ID 객체를 사용하여 메인 엔티티를 빌드합니다.
                        return UserSalesSummary.builder()
                                .id(newId) // .id() 메소드를 사용합니다.
                                .soldCount(0)
                                .totalRevenue(BigDecimal.ZERO)
                                .build();
                    });

            profit.setSoldCount((int) soldCount);
            profit.setTotalRevenue(total);
            profit.setLastUpdated(LocalDateTime.now(ZoneId.of("Asia/Seoul")));

            profitRepo.save(profit);
        }
    }
}