package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromptSummary {
    private Long promptId;
    private String promptName;
    private Integer price;
    private String aiInspectionRate;
    private String exampleContentUrl;
}
