package org.example.prumpt_be.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class PurchasedPromptDto {
    private Long promptId;
    private String promptName;
    private Integer price;
    private String ownerProfileName;
    private String thumbnailImageUrl;
    private String aiInspectionRate;
    private LocalDateTime purchasedAt; // 구매 일시
    private Long reviewId; // 작성한 리뷰 ID (null일 수 있음)
    private Double reviewRate; // 작성한 리뷰 평점 (null일 수 있음)
}