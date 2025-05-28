package org.example.prumpt_be.test;

import org.example.prumpt_be.domain.entity.*;
import org.example.prumpt_be.repository.*;
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
    private final PromptReviewsRepository promptReviewsRepo;

    public TestDataLoader(UsersRepository usersRepo,
                          PromptsRepository promptsRepo,
                          UserSalesSummaryRepository summaryRepo, PurchasesRepository purchasesRepository, PromptReviewsRepository promptReviewsRepo) {
        this.usersRepo    = usersRepo;
        this.promptsRepo  = promptsRepo;
        this.summaryRepo  = summaryRepo;
        this.purchasesRepo = purchasesRepository;
        this.promptReviewsRepo = promptReviewsRepo;
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
        createDummyPromptReviews();
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

    public void createDummyPromptReviews() {
        List<Prompts> prompts = promptsRepo.findAll(); //모든 프롬프트 데이터 조회
        List<Purchases> purchases = purchasesRepo.findAll(); //모든 거래이력 데이터 조회
        List<Users> users = usersRepo.findAll();
        Random random = new Random();

        for (Prompts prompt : prompts) {
            int reviewCount = random.nextInt(6); // 0~5개 리뷰 생성
            // 해당 프롬프트를 구매한 구매내역만 필터링
            List<Purchases> promptPurchases = purchases.stream()
                    .filter(p -> p.getPromptId().getPromptID() == prompt.getPromptID())
                    .toList();

            for (int i = 0; i < reviewCount && i < promptPurchases.size(); i++) {
                int randHalf = 1 + random.nextInt(10);
                Purchases purchase = promptPurchases.get(i);
                PromptReviews review = new PromptReviews();
                review.setPromptId(prompt);
                review.setPurchaseId(purchase);
                review.setRate(randHalf * 0.5);        // 0.5 단위 (1.0~5.0)
                review.setReviewer(purchase.getBuyer());
                // reviewedAt, updatedAt은 @PrePersist/@PreUpdate로 자동 설정
                // 필요시 직접 지정 가능: review.setReviewedAt(LocalDateTime.now());
                 promptReviewsRepo.save(review);
            }
        }
    }
}
