package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PromptAvgRateDto {
    private int promptId;
    private String promptName;
    private int ownerId;
    private Double avgRateFromEachPrompt;

    public PromptAvgRateDto(int ownerId, int promptId, String promptName, Double avgRateFromEachPrompt) {
        this.ownerId = ownerId;
        this.promptId = promptId;
        this.promptName = promptName;
        this.avgRateFromEachPrompt = avgRateFromEachPrompt;
    }

}
