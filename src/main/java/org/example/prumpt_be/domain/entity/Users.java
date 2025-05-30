package org.example.prumpt_be.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private int userID;

    @Column(nullable = false, unique = true)
    private String auth0Id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int emailVerified;

    @Column(nullable = false,  columnDefinition = "INT DEFAULT 0")
    private int point;

    @Column(nullable = false)
    private String profileName;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column
    private String profileImg_url;

    @Column
    private String bannerImg_url;

    @Column(nullable = false)
    private String user_role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;


    public Users(String sub, String email) {
        this.auth0Id = sub;
        this.email = email;
        this.emailVerified = 1; // 기본값: 이메일 인증됨
        this.point = 0; // 기본값: 포인트 0
        this.profileName = "사용자" + sub.substring(0, 5); // 기본 프로필명
        this.introduction = ""; // 기본 소개글
        this.profileImg_url = ""; // 기본 프로필 이미지 URL
        this.bannerImg_url = ""; // 기본 배너 이미지 URL
        this.user_role = "USER"; // 기본 사용자 역할
        this.createdAt = LocalDateTime.now();
    }
}
