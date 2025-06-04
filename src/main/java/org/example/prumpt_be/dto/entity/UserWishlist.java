package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_wishlist",
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_user_prompt_wish", columnNames = {"user_id", "prompt_id"})
       },
       indexes = {
           @Index(name = "idx_wishlist_user_id", columnList = "user_id"),
           @Index(name = "idx_wishlist_prompt_id", columnList = "prompt_id")
       }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private Long wishlistId; // 스키마는 INT지만 Long 사용 권장

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id", nullable = false)
    private Prompt prompt;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;
}