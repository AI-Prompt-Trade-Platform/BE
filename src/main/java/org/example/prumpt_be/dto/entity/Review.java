package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userID;

    @ManyToOne
    @JoinColumn(name = "prompt_id")
    private Prompt promptID;
}
