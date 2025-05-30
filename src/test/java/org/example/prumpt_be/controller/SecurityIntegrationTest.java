package org.example.prumpt_be.controller;

import org.example.prumpt_be.repository.*;
import org.example.prumpt_be.test.TestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.util.List;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2 사용하되 스키마 자동 생성
@TestPropertySource(locations = "classpath:application-test.properties")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
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
    private PromptReviewsRepository promptReviewRepository;

    @BeforeEach
    void setUp() {
        // 0) 리뷰
        promptReviewRepository.deleteAll();

        // 1) 요약(summary)
        userSalesSummaryRepository.deleteAll();

        // 2) 구매
        purchasesRepository.deleteAll();

        // 3) 프롬프트
        promptsRepository.deleteAll();

        // 4) 유저
        usersRepository.deleteAll();


        testDataLoader.loadAllDummyData();
    }

    //토큰 인증 안되었을때 401 Unauthorized 응답 테스트
    @Test
    void whenNoToken_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/monitoring/prompts"))
                .andDo(print()) // 요청과 응답을 가독성 좋게 출력
                .andExpect(status().isUnauthorized()); // 토큰 없이 접근 시 Unauthorized 응답

    }


    //토큰 인증 되었을때 200 OK 응답 테스트
    @Test
    void whenValidJwt_thenOk() throws Exception {
        // 방법 1: Jwt.Builder 람다 사용
        mockMvc.perform(get("/api/monitoring/prompts")
                        .with(jwt()
                                .jwt(jwt -> jwt
                                        .claim(JwtClaimNames.ISS, "https://dev-q64r0n0blzhir6y0.us.auth0.com/")
                                        .subject("auth0|1")
                                        .audience(List.of("https://dev-q64r0n0blzhir6y0.us.auth0.com/api/v2/"))
                                        .claim("email", "user@example.com")
                                )
                        )
                )
                .andDo(print()) // 요청과 응답을 가독성 좋게 출력
                .andExpect(status().isOk());
    }


    //
    @Test
    void whenPrebuiltJwt_thenOk() throws Exception {
        // 방법 2: 미리 만든 Jwt 전달
        Jwt jwt = Jwt.withTokenValue("dummy")
                .header("alg", "none")
                .issuer("https://dev-q64r0n0blzhir6y0.us.auth0.com/")
                .audience(List.of("https://dev-q64r0n0blzhir6y0.us.auth0.com/api/v2/"))
                .subject("auth0|1")
                .claim("email", "user@example.com")
                .build();

        mockMvc.perform(get("/api/monitoring/prompts")
                        .with(jwt().jwt(jwt))
                )
                .andDo(print()) // 요청과 응답을 가독성 좋게 출력
                .andExpect(status().isOk());
    }
}