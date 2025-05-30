package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.prumpt_be.dto.response.PromptSummary;

import java.util.List;

@Getter
@AllArgsConstructor
public class PromptListResponse {
    private List<PromptSummary> prompts;
}