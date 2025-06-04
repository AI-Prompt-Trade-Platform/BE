package org.example.prumpt_be.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BuyerDto {
    private Long userID;
    private String auth0Id;
    private Integer point;
    private String profileName;
    private String introduction;
    private String profileImg_url;
    private String bannerImg_url;
    private String user_role;
    private String createdAt;
    private String updatedAt;
}
