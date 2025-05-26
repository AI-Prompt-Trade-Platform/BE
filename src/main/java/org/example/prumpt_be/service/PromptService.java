package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.dto.ReviewDTO;
import org.example.prumpt_be.dto.request.PromptCreateRequestDTO;
import org.example.prumpt_be.dto.request.PromptUpdateRequestDTO;
import org.example.prumpt_be.entity.Category;
import org.example.prumpt_be.entity.Prompt;
import org.example.prumpt_be.entity.Tag;
import org.example.prumpt_be.entity.User;
import org.example.prumpt_be.repository.CategoryRepository;
import org.example.prumpt_be.repository.PromptRepository;
import org.example.prumpt_be.repository.TagRepository;
import org.example.prumpt_be.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromptService {

    private final PromptRepository promptRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    //프롬프트 상세 조회
    public PromptDetailDTO getPromptDetail(Long id) {
        return promptRepository.findById(id)
                .map(prompt -> {
                    double avgRating = prompt.getReviews().stream()
                            .mapToInt(r -> r.getRating())
                            .average()
                            .orElse(0);

                    return new PromptDetailDTO(
                            prompt.getId(),
                            prompt.getTitle(),
                            prompt.getDescription(),
                            prompt.getContent(),
                            prompt.getPrice(),
                            prompt.getAuthor().getUsername(),
                            prompt.getAuthor().getProfile(),
                            prompt.getCategory().getName(),
                            prompt.getTags().stream()
                                    .map(Tag::getName)
                                    .toList(),
                            false, // TODO 위시리스트와 연결 필요
                            avgRating,
                            prompt.getReviews().stream()
                                    .map(r -> new ReviewDTO(
                                            r.getUser().getUsername(),
                                            r.getRating(),
                                            r.getContent()))
                                    .toList()
                    );
                })
                .orElseThrow(() -> new RuntimeException("Prompt not found"));
    }

    //프롬프트 전체 조회
    public List<PromptDetailDTO> getAllPrompts() {
        return promptRepository.findAll().stream()
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
                            prompt.getTags().stream().map(Tag::getName).toList(),
                            false, //TODO 위시리스랑 연결필요
                            avgRating,
                            prompt.getReviews().stream()
                                    .map(r -> new ReviewDTO(r.getUser().getUsername(), r.getRating(), r.getContent()))
                                    .toList()
                    );
                })
                .toList();
    }


    //프롬프트 등록
    @Transactional
    public Long savePrompt(PromptCreateRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // TODO: Security 적용 후 인증된 사용자로 대체
        User author = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Prompt prompt = Prompt.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .content(dto.getContent())
                .price(dto.getPrice())
                .category(category)
                .author(author)
                .build();

        List<Tag> tagEntities = resolveTags(dto.getTags());
        prompt.setTags(tagEntities);

        promptRepository.save(prompt);
        return prompt.getId();
    }

    //태그 이름 목록 → Tag Entity 매핑
    private List<Tag> resolveTags(List<String> tagNames) {
        return tagNames.stream()
                .map(name -> tagRepository.findByName(name)
                        .orElseGet(() -> tagRepository.save(new Tag(name))))
                .collect(Collectors.toList());
    }

    //프롬프트 수정
    @Transactional
    public void updatePrompt(Long id, PromptUpdateRequestDTO dto) {
        Prompt prompt = promptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prompt not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<Tag> tagEntities = resolveTags(dto.getTags());

        prompt.setTitle(dto.getTitle());
        prompt.setDescription(dto.getDescription());
        prompt.setContent(dto.getContent());
        prompt.setPrice(dto.getPrice());
        prompt.setCategory(category);
        prompt.setTags(tagEntities);
    }

    //프롬프트 삭제
    @Transactional
    public void deletePrompt(Long id) {
        Prompt prompt = promptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prompt not found"));
        promptRepository.delete(prompt);
    }


}
