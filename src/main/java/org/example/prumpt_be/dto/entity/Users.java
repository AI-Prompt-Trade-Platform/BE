package org.example.prumpt_be.dto.entity;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userID;

    @Column(name = "auth0_id")
    private String auth0Id;

    @Column(name = "point")
    private Integer point;

    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "profile_img_url")
    private String profileImg_url;

    @Column(name = "banner_img_url")
    private String bannerImg_url;

    @Column(name = "user_role", nullable = false)
    private String user_role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();
        }
    }


    public Users(String sub) {
        this.auth0Id = sub;
        this.point = 0; // 기본값: 포인트 0
        this.profileName = "사용자" + sub.substring(0, 5); // 기본 프로필명
        this.introduction = ""; // 기본 소개글
        this.profileImg_url = ""; // 기본 프로필 이미지 URL
        this.bannerImg_url = ""; // 기본 배너 이미지 URL
        this.user_role = "USER"; // 기본 사용자 역할
        this.createdAt = LocalDateTime.now();
    }
}

