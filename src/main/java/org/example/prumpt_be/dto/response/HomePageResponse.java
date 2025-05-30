package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.prumpt_be.dto.response.PromptSummary;
import org.example.prumpt_be.dto.response.UserSummary;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class HomePageResponse {
    private List<PromptSummary> popularPrompts;
    private List<PromptSummary> latestPrompts;
    private List<PromptSummary> recommendedPrompts;
    private List<UserSummary> popularCreators;
}
