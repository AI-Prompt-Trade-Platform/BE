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

@Component
public class TestDataLoader {

    private final UsersRepository usersRepo;
    private final PromptsRepository promptsRepo;
    private final UserSalesSummaryRepository summaryRepo;
    private final PurchasesRepository purchasesRepository;

    public TestDataLoader(UsersRepository usersRepo,
                          PromptsRepository promptsRepo,
                          UserSalesSummaryRepository summaryRepo, PurchasesRepository purchasesRepository) {
        this.usersRepo    = usersRepo;
        this.promptsRepo  = promptsRepo;
        this.summaryRepo  = summaryRepo;
        this.purchasesRepository = purchasesRepository;
    }

    /**
     * 테스트 실행 전 @BeforeEach 등에서 호출하세요.
     */
    @Transactional
    public void loadAllDummyData() {
        createDummyUsers();
        createDummyPrompts();
        createDummyUserSalesSummaries();
        createDummyPurchases();
    }

    @Transactional
    public void createDummyUsers() {
        for (int i = 1; i <= 10; i++) {
            Users u = new Users();
            u.setUserID(i);
            u.setAuth0_id("auth0-user-" + i);
            u.setEmail("user" + i + "@example.com");
            u.setEmailVerified(1);
            u.setPoint(1000);
            u.setProfileName("User " + i);
            u.setIntroduction("This is user " + i);
            u.setProfileImg_url("https://example.com/avatar" + i + ".png");
            u.setBannerImg_url("https://example.com/banner" + i + ".png");
            u.setUser_role("ROLE_USER");
            u.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(10 - i));
            u.setUpdated_at(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            usersRepo.save(u);
        }
    }

    @Transactional
    public void createDummyPrompts() {
        List<Users> users = usersRepo.findAll();
        int nextPromptId = 1;
        for (Users owner : users) {
            Prompts p = new Prompts();
            p.setPromptID(nextPromptId++);
            p.setPrompt_name("Sample Prompt " + p.getPromptID());
            p.setPrompt_content("This is the content of prompt " + p.getPromptID());
            p.setPrice(100 * p.getPromptID());
            p.setAi_inspection_rate("0.0");
            p.setOwnerID(owner);
            p.setExample_content_url("https://example.com/prompt" + p.getPromptID() + "/example.png");
            p.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")).minusDays(p.getPromptID()));
            p.setUpdatedAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")));
            promptsRepo.save(p);
        }
    }

    @Transactional
    public void createDummyUserSalesSummaries() {
        List<Users> users = usersRepo.findAll();
        LocalDate summaryDate = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        LocalDateTime now     = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        for (int i = 0; i < users.size(); i++) {
            Users user = users.get(i);
            UserSalesSummary s = new UserSalesSummary();
            s.setUser(user);
            s.setSoldCount(i * 2 + 1);  // 예: 1, 3, 5, …
            s.setTotalRevenue(BigDecimal.valueOf((i * 2 + 1) * 100L));
            s.setSummaryDate(summaryDate); //유저가 10명이라서 10일전까지만 만들어짐
            s.setLastUpdated(now.minusHours(users.size() - i));
            summaryRepo.save(s);
        }
    }

    @Transactional
    public void createDummyPurchases() {
        // 1) 사용자와 프롬프트 리스트 조회
        List<Users> users   = usersRepo.findAll();
        List<Prompts> prompts = promptsRepo.findAll();

        // 2) 10건의 더미 구매 기록 생성
        for (int i = 0; i < 10; i++) {
            Purchases purchase = new Purchases();
            // Round-robin 방식으로 buyer, prompt 지정
            purchase.setBuyer(users.get(i % users.size()));
            purchase.setPromptId(prompts.get(i % prompts.size()));
            // purchasedAt은 @PrePersist 에서 현재 시간으로 자동 설정됨
            purchasesRepository.save(purchase);
        }
    }
}
