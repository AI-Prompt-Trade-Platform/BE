package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.prumpt_be.dto.response.PromptSummary;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMypageResponse {
    private Long userId;
    private String profileName;
    private String profileImgUrl;
    private String bannerImgUrl;
    private String introduction;
    private String userRole;
    private int point;
    private List<PromptSummary> sellingPrompts;
    private List<PromptSummary> completedPurchases;
    private List<PromptSummary> ongoingPurchases;
}