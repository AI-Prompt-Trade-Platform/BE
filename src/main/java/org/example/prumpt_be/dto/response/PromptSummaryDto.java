package org.example.prumpt_be.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
// todo: 프롬프트 설명 정보 필요
public class PromptSummaryDto {
    private Long promptId;
    private String promptName;
    private Integer price;
    private String ownerProfileName; // 판매자 프로필 이름
    private String thumbnailImageUrl; // 대표 이미지 (example_content_url 또는 별도 필드)
    private String aiInspectionRate; // AI 검수 등급 (필요시)
    private LocalDateTime createdAt; // 등록일 (최신 프롬프트 정렬 등에 활용)
    // 필요시 카테고리 정보 등 추가 가능
}