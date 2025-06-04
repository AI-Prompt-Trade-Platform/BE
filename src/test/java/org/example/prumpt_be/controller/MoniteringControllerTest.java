package org.example.prumpt_be.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.prumpt_be.dto.response.EachDaysProfitDto;
import org.example.prumpt_be.dto.response.PromptDto;
import org.example.prumpt_be.dto.response.RateAvgDto;
import org.example.prumpt_be.service.MoniteringService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2 사용하되 스키마 자동 생성
@TestPropertySource(locations = "classpath:application-test.properties")
public class MoniteringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private MoniteringService moniteringService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("/prompts API 정상 동작 테스트")
    void listPromptsTest() throws Exception {
        // given
        String userAuth0Id = "auth0|1";
        int userId = 1;
        List<PromptDto> prompts = Collections.emptyList();
        List<EachDaysProfitDto> profits = Collections.emptyList();
        RateAvgDto avgRate = new RateAvgDto(1,4.5); // Double -> RateAvgDto로 변경
        BigDecimal thisMonthProfit = BigDecimal.valueOf(10000);
        Long totalSalesCount = 5L;

        Mockito.when(moniteringService.getUserIdByAuth0Id(userAuth0Id)).thenReturn(userId);
        Mockito.when(moniteringService.getAllPromptsByOwnerId(userId)).thenReturn(prompts);
        Mockito.when(moniteringService.findDailyRevenueWithZero(eq(userId), any())).thenReturn(profits);
        Mockito.when(moniteringService.getAvgRateOfAllPromptsByUserId(userId)).thenReturn(avgRate);
        Mockito.when(moniteringService.getProfitOfThisMonthByUserId(userId)).thenReturn(thisMonthProfit);
        Mockito.when(moniteringService.getTotalSalesCountByUserId(userId)).thenReturn(totalSalesCount);

        // JWT Mocking
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "HS256")
                .claim("sub", userAuth0Id)
                .build();

        // when & then
        mockMvc.perform(get("/api/monitoring/prompts")
                        .with(jwt().jwt(jwt)) // 인증 정보 SecurityContext에 주입
                        .param("period", "MONTH"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.allPrompts").isArray())
                .andExpect(jsonPath("$.eachDaysProfits").isArray())
                .andExpect(jsonPath("$.avgRate").value(avgRate))
                .andExpect(jsonPath("$.thisMonthProfit").value(thisMonthProfit.intValue()))
                .andExpect(jsonPath("$.totalSalesCount").value(totalSalesCount));
    }
}
