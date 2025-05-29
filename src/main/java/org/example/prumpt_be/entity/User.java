package org.example.prumpt_be.entity;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "auth0_id")
    private String auth0Id;

    @Column(name = "email")
    private String email;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "point")
    private Integer point;

    @Column(name = "profile_name")
    private String profileName;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "banner_img_url")
    private String bannerImgUrl;

    @Column(name = "user_role")
    private String userRole;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}