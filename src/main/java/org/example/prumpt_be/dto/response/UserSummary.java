package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserSummary {
    private Long userId;
    private String profileName;
    private String profileImgUrl;
}