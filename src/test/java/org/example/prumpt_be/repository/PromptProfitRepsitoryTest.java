package org.example.prumpt_be.repository;

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
import java.util.List;
import java.util.Locale;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PromptProfitRepsitoryTest {

    @Autowired
    private TestDataLoader testDataLoader;

    //더미데이터 생성(Prompts, Users, Purchases)
    @BeforeEach
    void createDummy() {
        testDataLoader.loadAllDummyData();
    }

    @Autowired
    private PromptsRepository promptsRepository;
    @Autowired
    private PurchasesRepository purchasesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserSalesSummaryRepository userSalesSummaryRepository;

    @BeforeEach
    void setUp() {
        // 1) 종속 테이블(자식)부터
        userSalesSummaryRepository.deleteAll();      // UserSalesSummary
        purchasesRepository.deleteAll();    // Purchases
        // 2) 다른 종속 관계(예: Prompt → Users)도 고려
        promptsRepository.deleteAll();      // Prompts
        usersRepository.deleteAll();        // Users

    }


    @Test
    @DisplayName("특정사용자, 기간으로 수익 조회")
    void FindYesterdayRevenueTest() {
        setUp();
        createDummy();
        LocalDate start = LocalDate.parse("2025-05-20");    // 기본 ISO 포맷
        LocalDate end   = LocalDate.parse("2025-05-25");

        BigDecimal revenue = userSalesSummaryRepository.findTotalRevenueByUserIdAndPeriod(2, start, end);
        String formatted = NumberFormat.getNumberInstance(Locale.KOREA).format(revenue);
        System.out.printf("사용자ID: %d, 기간: %s ~ %s, 총 수익: %s원%n", 2, start, end, formatted);
    }

    @Test
    @DisplayName("차트 그리기 위한 각 날짜별 전체 데이터 조회")
    void FindAllRevenueTest() {
        setUp();
        createDummy();
        LocalDate start = LocalDate.parse("2025-05-17");    // 기본 ISO 포맷
        LocalDate end   = LocalDate.parse("2025-05-25");

        List<EachDaysProfitDto> dailyList = userSalesSummaryRepository.findDailyRevenueByUserAndPeriod(3, start, end);
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.KOREA);

        System.out.println("날짜별 수익 내역:");
        for (EachDaysProfitDto dto : dailyList) {
            String formattedRevenue = formatter.format(dto.getTotalRevenue());
            System.out.printf("날짜: %s, 수익: %s원%n", dto.getSummaryDate(), formattedRevenue);
        }
    }
}
