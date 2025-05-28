package org.example.prumpt_be.repository;

import org.example.prumpt_be.domain.entity.UserSalesSummary;
import org.example.prumpt_be.dto.response.EachDaysProfitDto;
import org.example.prumpt_be.test.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PromptProfitRepsitoryTest {

    @Autowired
    private TestDataLoader testDataLoader;
    @Autowired
    private PromptsRepository promptsRepository;
    @Autowired
    private PurchasesRepository purchasesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserSalesSummaryRepository userSalesSummaryRepository;
    @Autowired
    private PromptReviewsRepository reviewsRepository;

    @BeforeEach
    void setUp() {
        // 1) 종속 테이블(자식)부터
        userSalesSummaryRepository.deleteAll();      // UserSalesSummary
        purchasesRepository.deleteAll();    // Purchases
        // 2) 다른 종속 관계(예: Prompt → Users)도 고려
        promptsRepository.deleteAll();      // Prompts
        usersRepository.deleteAll();        // Users

        testDataLoader.loadAllDummyData();
    }


//    =========================== 테스트 코드 ===============================
    @Test
    @DisplayName("특정사용자, 기간으로 총 수익 조회")
    void FindTotalProfitByUserIDTest() {
        LocalDate start = LocalDate.parse("2024-05-27");    // 기본 ISO 포맷
        LocalDate end   = LocalDate.parse("2025-05-28");

        BigDecimal revenue = userSalesSummaryRepository.findTotalRevenueByUserIdAndPeriod(3, start, end);
        String formatted = NumberFormat.getNumberInstance(Locale.KOREA).format(revenue);
        System.out.printf("사용자ID: %d, 기간: %s ~ %s, 총 수익: %s원%n", 3, start, end, formatted);
    }

    @Test
    @DisplayName("특정 기간동안 수익이 있던 날들의 데이터 조회 (차트 그리기 용도)") //todo: 수익 0원인날은 안나와서 FE에서 처리해서 그려야함
    void FindAllProfitsDaysTest() {
        LocalDate start = LocalDate.parse("2025-05-20");    // 기본 ISO 포맷
        LocalDate end   = LocalDate.parse("2025-05-27");

        List<EachDaysProfitDto> dailyList = userSalesSummaryRepository.findDailyRevenueByUserAndPeriod(3, start, end);
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);

        System.out.println("날짜별 수익 내역:");
        for (EachDaysProfitDto dto : dailyList) {
            String formattedRevenue = formatter.format(dto.getTotalRevenue());
            System.out.printf("기간: %s ~ %s | 날짜: %s | 수익: %s원%n", start, end, dto.getSummaryDate(), formattedRevenue);
        }
    }

    @Test
    @DisplayName("조회한 날의 어제자 수익 조회")
    void FindYesterdayProfitTest() {
        int userId = 2;
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        BigDecimal yesterdayProfit = userSalesSummaryRepository.findYesterdayRevenueByUserId(userId, yesterday);
        System.out.printf("사용자: %d | 어제 수익: %s%n", userId ,yesterdayProfit);
    }

    @Test
    @DisplayName("특정 유저의 프롬프트들 전체 별점 평균 조회")
    void FindAvgFromPromptsTest() {
        int userId = 3;
        Double avgRate = reviewsRepository.findAvgRateOfAllPromptsByUserId(userId);
        System.out.printf("사용자: %d | 평균별점: %s%n", userId, avgRate);
    }
}
