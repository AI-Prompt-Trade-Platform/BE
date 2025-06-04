package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "auth0_id")
    private String auth0Id;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "point")
    private Integer point;

    @Column(name = "profile_name")
    private String username;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "profile_img_url")
    private String profile;

    @Column(name = "banner_img_url")
    private String banner;

    @Column(name = "user_role")
    private String role;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}
