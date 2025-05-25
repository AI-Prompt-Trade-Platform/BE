package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.dto.ReviewDTO;
import org.example.prumpt_be.repository.PromptRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptRepository promptRepository;

    public PromptDetailDTO getPromptDetail(Long id) {
        return promptRepository.findById(id)
                .map(prompt -> {
                    double avgRating = prompt.getReviews().stream()
                            .mapToInt(r -> r.getRating())
                            .average().orElse(0);

                    return new PromptDetailDTO(
                            prompt.getId(),
                            prompt.getTitle(),
                            prompt.getDescription(),
                            prompt.getContent(),
                            prompt.getPrice(),
                            prompt.getAuthor().getUsername(),
                            prompt.getAuthor().getProfile(),
                            prompt.getCategory().getName(),
                            prompt.getTags().stream().map(tag -> tag.getName()).toList(),
                            false, // 북마크 처리 미구현
                            avgRating,
                            prompt.getReviews().stream()
                                    .map(r -> new ReviewDTO(r.getUser().getUsername(), r.getRating(), r.getContent()))
                                    .toList()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Prompt not found"));
    }
}

