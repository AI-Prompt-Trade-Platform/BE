package org.example.prumpt_be.dto;

import lombok.*;
import org.example.prumpt_be.dto.entity.PromptClassification;
import org.example.prumpt_be.dto.response.ClassificationDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PromptDetailDTO {
    private Long id;
    private String title;
    private String description;
    private String content;
    private int price;
    private String ownerProfileName; // 판매자 프로필 이름
//    private String category;
//    private List<String> tags;
    private boolean isBookmarked;
    private double averageRating;
    private List<ReviewDTO> reviews;
    private ClassificationDTO categories;
    private String thumbnailImageUrl; // 대표 이미지 (example_content_url 또는 별도 필드)
    private String aiInspectionRate; // AI 검수 등급 (필요시)
    private LocalDateTime createdAt; // 등록일 (최신 프롬프트 정렬 등에 활용)

}
