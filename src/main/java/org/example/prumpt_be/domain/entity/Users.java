package org.example.prumpt_be.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {
    @Id
    @Column(name = "userID")
    private int userID;

    @Column(nullable = false, unique = true)
    private String auth0_id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean emailVerified;

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
}
