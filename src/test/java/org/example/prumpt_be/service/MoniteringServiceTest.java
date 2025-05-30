package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.response.EachDaysProfitDto;
import org.example.prumpt_be.repository.PromptsRepository;
import org.example.prumpt_be.repository.PurchasesRepository;
import org.example.prumpt_be.repository.UserSalesSummaryRepository;
import org.example.prumpt_be.repository.UsersRepository;
import org.example.prumpt_be.test.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class MoniteringServiceTest {


    @Autowired
    private PromptsRepository promptsRepository;
    @Autowired
    private PurchasesRepository purchasesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserSalesSummaryRepository userSalesSummaryRepository;
    @Autowired
    private MoniteringService moniteringService;
    @Autowired
    private TestDataLoader testDataLoader; //더미데이터 생성해주는 클래스

    @BeforeEach
    void setUp() {
        // 1) 종속 테이블(자식)부터
        userSalesSummaryRepository.deleteAll();      // UserSalesSummary
        purchasesRepository.deleteAll();    // Purchases
        // 2) 다른 종속 관계(예: Prompt → Users)도 고려
        promptsRepository.deleteAll();      // Prompts
        usersRepository.deleteAll();        // Users
    }

//    ====================== 더미데이터 생성 메소드들 (TestDataLoader클래스에서 생성해주긴 하는데, 혹시몰라서 둠) =============================
//    //유저 더미데이터
//    public void createDummyUsers() {
//        for (int i = 1; i <= 10; i++) {
//            Users user = new Users();
////            user.setUserID(i); //자동증가로 바꿔서 DB가 처리
//            user.setAuth0_id("auth0-user-" + i);
//            user.setEmail("user" + i + "@example.com");
//            user.setEmailVerified(1);
//            user.setPoint(1000);
//            user.setProfileName("User " + i);
//            user.setIntroduction("This is user " + i);
//            user.setProfileImg_url("https://example.com/avatar" + i + ".png");
//            user.setBannerImg_url("https://example.com/banner" + i + ".png");
//            user.setUser_role("ROLE_USER");
//            user.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(10 - i));
//            user.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
//            usersRepository.save(user);
//        }
//    }
//
//    //프롬프트 더미데이터
//    public void createDummyPrompts() {
//        List<Users> users = usersRepository.findAll();
//        int promptSeq = 1;
//        for (Users user : users) {
//            for (int i = 1; i <= 5; i++) {
//                Prompts prompt = new Prompts();
//                // promptID는 auto-increment이므로 set하지 않음
//                prompt.setPrompt_name("User" + user.getUserID() + " Prompt " + i);
//                prompt.setPrompt_content("Content for User" + user.getUserID() + "'s Prompt " + i);
//                prompt.setPrice(1000 + i * 100);
//                prompt.setAi_inspection_rate("0.0");
//                prompt.setOwnerID(user);
//                prompt.setExample_content_url("https://example.com/user" + user.getUserID() + "/prompt" + i + ".png");
//                prompt.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(promptSeq));
//                prompt.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
//                promptsRepository.save(prompt);
//                promptSeq++;
//            }
//        }
//    }
//
//    //거래 더미데이터
//    public void createDummyPurchases() {
//        List<Users> users = usersRepository.findAll();
//        List<Prompts> prompts = promptsRepository.findAll();
//        Random random = new Random();
//
//        // 예시: 30건의 랜덤 구매 기록 생성
//        for (int i = 0; i < 30; i++) {
//            Purchases purchase = new Purchases();
//            Users buyer = users.get(random.nextInt(users.size()));
//            Prompts prompt = prompts.get(random.nextInt(prompts.size()));
//
//            purchase.setBuyer(buyer);
//            purchase.setPromptId(prompt);
//            // purchasedAt은 @PrePersist로 자동 설정됨
//            purchasesRepository.save(purchase);
//        }
//    }
//
//    //판매 요약 더미데이터
//    public void createDummyUserSalesSummaries() {
//        List<Users> users = usersRepository.findAll();
//        List<Purchases> purchases = purchasesRepository.findAll();
//        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
//
//        for (Users user : users) {
//            // 해당 유저가 소유한 프롬프트 ID 목록
//            List<Integer> ownedPromptIds = promptsRepository.findAll().stream()
//                    .filter(p -> p.getOwnerID().getUserID() == user.getUserID())
//                    .map(Prompts::getPromptID)
//                    .toList();
//
//            // 해당 유저의 프롬프트가 판매된 Purchases만 필터링
//            List<Purchases> userPromptSales = purchases.stream()
//                    .filter(pur -> ownedPromptIds.contains(pur.getPromptId().getPromptID()))
//                    .toList();
//
//            int soldCount = userPromptSales.size();
//            int totalRevenue = userPromptSales.stream()
//                    .mapToInt(pur -> pur.getPromptId().getPrice())
//                    .sum();
//
//            // 랜덤 날짜 생성 (예: 지난 30일 내)
//            LocalDate randomDate = LocalDate.ofEpochDay(
//                    ThreadLocalRandom.current().nextLong(
//                            LocalDate.now().minusDays(30).toEpochDay(),
//                            LocalDate.now().toEpochDay()
//                    )
//            );
//
//            UserSalesSummary summary = new UserSalesSummary();
//            summary.setUserId(user.getUserID()); // 복합키 필수
//            summary.setSummaryDate(randomDate); // 랜덤 날짜 설정
//            summary.setUser(usersRepository.findById(user.getUserID()).orElseThrow());
//            summary.setSoldCount(soldCount);
//            summary.setTotalRevenue(BigDecimal.valueOf(totalRevenue));
//            summary.setLastUpdated(now);
//
//            userSalesSummaryRepository.save(summary);
//        }
//    }

//    =======================================================================




    public enum PeriodType {
        MONTH, HALF_YEAR, YEAR
    }

    @Test
    @DisplayName("차트 생성용 특정기간의 일일수익 조회")
    public void findDailyRevenueWithZero() {
//        createDummyUsers();
//        createDummyPrompts();
//        createDummyPurchases();
//        createDummyUserSalesSummaries();
        testDataLoader.loadAllDummyData();


        List<EachDaysProfitDto> EachDayProfitList = moniteringService.findDailyRevenueWithZero(3, MoniteringService.PeriodType.MONTH);
        for (EachDaysProfitDto eachDaysProfitDto : EachDayProfitList) {
            System.out.println("사용자 : " + eachDaysProfitDto.getUserId() + " | " + "날짜" + eachDaysProfitDto.getSummaryDate() + " | " + "수익 : " + eachDaysProfitDto.getTotalRevenue());
        }
    }
}
