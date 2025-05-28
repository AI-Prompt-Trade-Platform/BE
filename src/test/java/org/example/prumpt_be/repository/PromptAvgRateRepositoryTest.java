package org.example.prumpt_be.repository;

import org.example.prumpt_be.domain.entity.PromptReviews;
import org.example.prumpt_be.test.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
public class PromptAvgRateRepositoryTest {
    @Autowired
    private TestDataLoader testDataLoader;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PromptReviewsRepository promptReviewsRepository;
    @Autowired
    private PromptsRepository promptsRepository;
    @Autowired
    private PurchasesRepository purchasesRepository;

    @BeforeEach
    void setUp() {
        // 1) 종속 테이블(자식)부터
        purchasesRepository.deleteAll();    // Purchases
        // 2) 다른 종속 관계(예: Prompt → Users)도 고려
        promptsRepository.deleteAll();      // Prompts
        usersRepository.deleteAll();        // Users
        // 3) 더미데이터 생성
        testDataLoader.loadAllDummyData();
    }


    @Test
    @DisplayName("특정 유저의 프롬프트들 전체 별점 평균 조회")
    void FindUsersAvgRateTest() {
        int userId = 2;
        Double AvgRate = promptReviewsRepository.findAvgRateOfAllPromptsByUserId(userId);
        System.out.printf("%s 번 사용자의 전체평균 별점 : %s%n", userId, AvgRate);
    }
}
