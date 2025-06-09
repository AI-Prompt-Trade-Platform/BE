//package org.example.prumpt_be.dto.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "users", indexes = {
//        @Index(name = "idx_users_auth0_id", columnList = "auth0_id", unique = true),
//        @Index(name = "idx_users_email", columnList = "email", unique = true)
//})
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL의 AUTO_INCREMENT를 사용하려면 IDENTITY 전략이 적합할 수 있습니다. 스키마상 user_id가 INT PRIMARY KEY로 되어있어, 자동 생성이 아니라면 이 부분은 수정이 필요합니다. 스키마에는 user_id가 자동생성이 아니지만, 일반적으로 PK는 자동생성을 많이 사용합니다. 여기서는 자동 생성을 가정하겠습니다. 만약 Auth0 ID를 PK로 사용하거나 다른 전략이 있다면 알려주세요.
//    @Column(name = "user_id")
//    private Long userId; // 스키마는 INT이지만, JPA에서는 Long을 사용하는 것이 일반적입니다.
//
//    @Column(name = "auth0_id", nullable = false, unique = true, length = 255)
//    private String auth0Id;
//
//    @Column(nullable = false, unique = true, length = 255)
//    private String email;
//
//    @Column(name = "email_verified", nullable = false)
//    @Builder.Default
//    private boolean emailVerified = false; // TINYINT(1)은 boolean으로 매핑
//
//    @Column(nullable = false)
//    @Builder.Default
//    private Integer point = 0;
//
//    @Column(name = "profile_name", nullable = false, length = 255)
//    private String profileName;
//
//    @Lob // TEXT 타입 매핑
//    @Column(columnDefinition = "TEXT")
//    private String introduction;
//
//    @Column(name = "profile_img_url", length = 255)
//    private String profileImgUrl;
//
//    @Column(name = "banner_img_url", length = 255)
//    private String bannerImgUrl;
//
//    @Column(name = "user_role", nullable = false, length = 255)
//    private String userRole; // TODO: Enum 타입으로 변경 고려 (e.g., Role.USER, Role.ADMIN)
//
//    @CreationTimestamp
//    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    // 연관관계 매핑 (User가 주인이 아닌 경우)
//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<Prompt> prompts = new ArrayList<>(); // 판매 중인 프롬프트
//
//    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<PromptPurchase> purchases = new ArrayList<>(); // 구매한 프롬프트
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<UserWishlist> wishlists = new ArrayList<>();
//
//    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<PromptReview> reviews = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<UserSalesSummary> salesSummaries = new ArrayList<>();
//}