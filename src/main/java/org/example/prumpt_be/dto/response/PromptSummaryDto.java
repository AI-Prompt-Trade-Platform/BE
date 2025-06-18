package org.example.prumpt_be.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Builder
// todo: 프롬프트 설명 정보 필요
public class PromptSummaryDto {
    // 기존 필드
    private Long promptId;
    private String promptName;
    private Integer price;
    private String ownerProfileName;
    private String thumbnailImageUrl;
    private String aiInspectionRate;
    private LocalDateTime createdAt;

    // todo 주석에서 언급된 추가 필드들
    private String description;
    private String typeCategory; // 예: "image-generation", "text-summarization" 등 타입 카테고리 이름
    private Double rate; // 평균 별점
    private Integer salesCount; // 판매 수
    private List<String> hashTags; // 해시태그 이름 목록
}