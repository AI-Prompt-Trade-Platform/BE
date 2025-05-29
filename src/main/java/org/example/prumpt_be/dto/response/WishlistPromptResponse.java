package org.example.prumpt_be.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistPromptResponse {
    private Long promptId;
    private String promptName;
    private Integer price;
    private String exampleContentUrl;
    private String ownerName;
}