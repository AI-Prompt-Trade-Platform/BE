package org.example.prumpt_be.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileDto {
    private Long userId;
    private String email;
    private String profileName;
    private String introduction;
    private String profileImgUrl;
    private String bannerImgUrl;
    private Integer point;
    // 필요시 추가 정보 (예: 판매 건수, 구매 건수 등)
}