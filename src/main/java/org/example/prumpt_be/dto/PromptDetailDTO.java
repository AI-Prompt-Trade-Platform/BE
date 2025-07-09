package org.example.prumpt_be.dto;

import lombok.*;
import org.example.prumpt_be.dto.response.ClassificationDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PromptDetailDTO {
    private Long id; //프롬프트 ID
    private String auth0id; //JWT ID
    private String title; //프롬프트 제목
    private String description; //프롬프트에 대한 설명
    private String content; //프롬프트 본문
    private int price; //가격
    private String ownerProfileName; // 판매자 프로필 이름
    private boolean isBookmarked; // 위시리스트 추가 여부
    private double averageRating; // 평균 별점
    private List<ReviewDTO> reviews; // 전체 리뷰 리스트
    private ClassificationDTO categories; // 카테고리 정보
    private String thumbnailImageUrl; // 대표 이미지 (example_content_url 또는 별도 필드)
    private String aiInspectionRate; // AI 검수 등급 (필요시)
    private LocalDateTime createdAt; // 등록일 (최신 프롬프트 정렬 등에 활용)
    private boolean userPurchased; // 현재 사용자가 구매한 프롬프트인지 여부
}
