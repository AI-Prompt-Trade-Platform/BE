package org.example.prumpt_be.test;

import org.example.prumpt_be.domain.entity.Prompts;
import org.example.prumpt_be.domain.entity.Purchases;
import org.example.prumpt_be.domain.entity.UserSalesSummary;
import org.example.prumpt_be.domain.entity.Users;
import org.example.prumpt_be.repository.PromptsRepository;
import org.example.prumpt_be.repository.PurchasesRepository;
import org.example.prumpt_be.repository.UserSalesSummaryRepository;
import org.example.prumpt_be.repository.UsersRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class TestDataLoader {

    private final UsersRepository usersRepo;
    private final PromptsRepository promptsRepo;
    private final UserSalesSummaryRepository summaryRepo;
    private final PurchasesRepository purchasesRepo;

    public TestDataLoader(UsersRepository usersRepo,
                          PromptsRepository promptsRepo,
                          UserSalesSummaryRepository summaryRepo, PurchasesRepository purchasesRepository) {
        this.usersRepo    = usersRepo;
        this.promptsRepo  = promptsRepo;
        this.summaryRepo  = summaryRepo;
        this.purchasesRepo = purchasesRepository;
    }

    /**
     * 테스트 실행 전 @BeforeEach 등에서 호출하세요.
     */
    @Transactional
    public void loadAllDummyData() {
        createDummyUsers();
        createDummyPrompts();
        createDummyPurchases();
        createDummyUserSalesSummaries();
    }

    //유저 더미데이터
    public void createDummyUsers() {
        for (int i = 1; i <= 10; i++) {
            Users user = new Users();
//            user.setUserID(i); //자동증가로 바꿔서 DB가 처리
            user.setAuth0_id("auth0-user-" + i);
            user.setEmail("user" + i + "@example.com");
            user.setEmailVerified(1);
            user.setPoint(1000);
            user.setProfileName("User " + i);
            user.setIntroduction("This is user " + i);
            user.setProfileImg_url("https://example.com/avatar" + i + ".png");
            user.setBannerImg_url("https://example.com/banner" + i + ".png");
            user.setUser_role("ROLE_USER");
            user.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(10 - i));
            user.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            usersRepo.save(user);
        }
    }

    //프롬프트 더미데이터
    public void createDummyPrompts() {
        List<Users> users = usersRepo.findAll();
        int promptSeq = 1;
        for (Users user : users) {
            for (int i = 1; i <= 5; i++) {
                Prompts prompt = new Prompts();
                // promptID는 auto-increment이므로 set하지 않음
                prompt.setPrompt_name("User" + user.getUserID() + " Prompt " + i);
                prompt.setPrompt_content("Content for User" + user.getUserID() + "'s Prompt " + i);
                prompt.setPrice(1000 + i * 100);
                prompt.setAi_inspection_rate("0.0");
                prompt.setOwnerID(user);
                prompt.setExample_content_url("https://example.com/user" + user.getUserID() + "/prompt" + i + ".png");
                prompt.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(promptSeq));
                prompt.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
                promptsRepo.save(prompt);
                promptSeq++;
            }
        }
    }

    //거래 더미데이터
    public void createDummyPurchases() {
        List<Users> users = usersRepo.findAll();
        List<Prompts> prompts = promptsRepo.findAll();
        Random random = new Random();

        // 예시: 30건의 랜덤 구매 기록 생성
        for (int i = 0; i < 30; i++) {
            Purchases purchase = new Purchases();
            Users buyer = users.get(random.nextInt(users.size()));
            Prompts prompt = prompts.get(random.nextInt(prompts.size()));

            purchase.setBuyer(buyer);
            purchase.setPromptId(prompt);
            // purchasedAt은 @PrePersist로 자동 설정됨
            purchasesRepo.save(purchase);
        }
    }

    //판매 요약 더미데이터
    public void createDummyUserSalesSummaries() {
        List<Users> users = usersRepo.findAll();
        List<Purchases> purchases = purchasesRepo.findAll();
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        for (Users user : users) {
            // 해당 유저가 소유한 프롬프트 ID 목록
            List<Integer> ownedPromptIds = promptsRepo.findAll().stream()
                    .filter(p -> p.getOwnerID().getUserID() == user.getUserID())
                    .map(Prompts::getPromptID)
                    .toList();

            // 해당 유저의 프롬프트가 판매된 Purchases만 필터링
            List<Purchases> userPromptSales = purchases.stream()
                    .filter(pur -> ownedPromptIds.contains(pur.getPromptId().getPromptID()))
                    .toList();

            int soldCount = userPromptSales.size();
            int totalRevenue = userPromptSales.stream()
                    .mapToInt(pur -> pur.getPromptId().getPrice())
                    .sum();

            // 랜덤 날짜 생성 (예: 지난 30일 내)
            LocalDate randomDate = LocalDate.ofEpochDay(
                    ThreadLocalRandom.current().nextLong(
                            LocalDate.now().minusDays(30).toEpochDay(),
                            LocalDate.now().toEpochDay()
                    )
            );

            UserSalesSummary summary = new UserSalesSummary();
            summary.setUserId(user.getUserID()); // 복합키 필수
            summary.setSummaryDate(randomDate); // 랜덤 날짜 설정
            summary.setUser(usersRepo.findById(user.getUserID()).orElseThrow());
            summary.setSoldCount(soldCount);
            summary.setTotalRevenue(BigDecimal.valueOf(totalRevenue));
            summary.setLastUpdated(now);

            summaryRepo.save(summary);
        }
    }
}
