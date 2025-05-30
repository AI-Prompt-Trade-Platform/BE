package org.example.prumpt_be.dto.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "prompts")
//@Column(name = "type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promptId;

    private String promptName;

    @Lob
    private String promptContent;

    private Integer price;

    private String aiInspectionRate;

    private Long ownerId;

    private String exampleContentUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String type;

    private String model;
}
