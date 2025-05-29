package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class User {
    @Id
    private Long userId;

    @Column(nullable = false, unique = true)
    private String auth0Id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean emailVerified;

    @Column(nullable = false)
    private Integer point;

    @Column(nullable = false)
    private String profileName;

    private String introduction;
    private String profileImgUrl;
    private String bannerImgUrl;

    @Column(nullable = false)
    private String userRole;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}