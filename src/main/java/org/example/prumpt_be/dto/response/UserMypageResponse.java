package org.example.prumpt_be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserMypageResponse {
    private Long userId;
    private String profileName;
    private String profileImgUrl;
    private String bannerImgUrl;
    private String introduction;
    private String userRole;
    private Integer point;
}