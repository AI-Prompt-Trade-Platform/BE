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
    @Column(name = "user_id")
    private Long userId;

    private String auth0Id;
    private String email;
    private boolean emailVerified;
    private int point;
    private String profileName;
    private String introduction;
    private String profileImgUrl;
    private String bannerImgUrl;
    private String userRole;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
