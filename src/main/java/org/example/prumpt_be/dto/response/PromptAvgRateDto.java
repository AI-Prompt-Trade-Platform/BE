package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

//사용자의 각 프롬프트별 평균 평점 DTO
@Getter
public class PromptAvgRateDto {
    private final int ownerId;
    private final int promptId;
    private final String promptName;
    private final Double avgRateFromEachPrompt;

    public PromptAvgRateDto(int ownerId,
                            int promptId,
                            String promptName,
                            Double avgRateFromEachPrompt) {
        this.ownerId  = ownerId;
        this.promptId = promptId;
        this.promptName = promptName;
        this.avgRateFromEachPrompt = avgRateFromEachPrompt;
    }
}
