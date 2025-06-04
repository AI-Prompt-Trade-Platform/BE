package org.example.prumpt_be.dto.entity;


import lombok.Getter;
import lombok.Setter;
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
}

