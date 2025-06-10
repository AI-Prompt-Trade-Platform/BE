//package org.example.prumpt_be.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.prumpt_be.dto.PromptDetailDTO;
//import org.example.prumpt_be.dto.ReviewDTO;
//import org.example.prumpt_be.dto.entity.*;
//import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
//import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
//import org.example.prumpt_be.dto.request.PromptUpdateRequestDto;
//import org.example.prumpt_be.repository.*;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class PromptService {
//
//    private final PromptRepository promptRepository;
//    private final UserRepository userRepository;
//    private final TagRepository tagRepository;
//    private final PromptClassificationRepository promptClassificationRepository;
//    private final ModelCategoryRepository modelCategoryRepository;
//    private final TypeCategoryRepository typeCategoryRepository;
//
//    // 프롬프트 상세 조회
//    public PromptDetailDTO getPromptDetail(Long id) {
//        return promptRepository.findById(id)
//                .map(prompt -> {
//                    double avgRating = prompt.getReviews().stream()
//                            .mapToInt(Review::getRating)
//                            .average().orElse(0);
//
//                    PromptClassification classification = prompt.getClassification();
//
//                    return new PromptDetailDTO(
//                            prompt.getPromptId(),
//                            prompt.getPromptName(),
//                            prompt.getDescription(),
//                            prompt.getPromptContent(),
//                            prompt.getPrice(),
//                            prompt.getOwnerID().getProfileName(),
//                            "", // categoryName 미사용
//                            prompt.getTags().stream().map(Tag::getName).toList(),
//                            false, // 위시리스트 여부 추후 구현
//                            avgRating,
//                            prompt.getReviews().stream()
//                                    .map(r -> new ReviewDTO(r.getUserID().getProfileName(), r.getRating(), r.getContent()))
//                                    .toList(),
//                            classification != null ? classification.getModelCategory().getModelName() : "",
//                            classification != null ? classification.getTypeCategory().getTypeName() : ""
//                    );
//                })
//                .orElseThrow(() -> new RuntimeException("Prompt not found"));
//    }
//
//    // 프론프트 전체 조회
//    public List<PromptDetailDTO> getAllPrompts() {
//        return promptRepository.findAll().stream()
//                .map(prompt -> {
//                    double avgRating = prompt.getReviews().stream()
//                            .mapToInt(Review::getRating)
//                            .average().orElse(0);
//
//                    PromptClassification classification = prompt.getClassification();
//
//                    return new PromptDetailDTO(
//                            prompt.getPromptID(),
//                            prompt.getPromptName(),
//                            prompt.getDescription(),
//                            prompt.getPromptContent(),
//                            prompt.getPrice(),
//                            prompt.getOwnerID().getProfileName(),
//                            "", // categoryName 미사용
//                            prompt.getTags().stream().map(Tag::getName).toList(),
//                            false,
//                            avgRating,
//                            prompt.getReviews().stream()
//                                    .map(r -> new ReviewDTO(r.getUserID().getProfileName(), r.getRating(), r.getContent()))
//                                    .toList(),
//                            classification != null ? classification.getModelCategory().getModelName() : "",
//                            classification != null ? classification.getTypeCategory().getTypeName() : ""
//                    );
//                })
//                .toList();
//    }
//
//    // 프론프트 등록
//    @Transactional
//    public Long savePrompt(PromptCreateRequestDto dto) {
//        System.out.println("📌 modelCategoryId: " + dto.getModelCategoryId());
//        System.out.println("📌 typeCategoryId: " + dto.getTypeCategoryId());
//        System.out.println("🔥 title: " + dto.getTitle());
//
//
//        Users author = userRepository.findById(1L) // TODO: 인증 사용자로 대체
//                .orElseThrow(() -> new RuntimeException("Author not found"));
//
//        Prompt prompt = Prompt.builder()
//                .promptName(dto.getTitle())
//                .promptContent(dto.getContent())
//                .price(dto.getPrice())
//                .aiInspectionRate(dto.getAiInspectionRate())
//                .exampleContentUrl(dto.getExampleContentUrl())
//                .ownerID(author)
//                .createdAt(LocalDateTime.now())
//                .build();
//
//        List<Tag> tagEntities = resolveTags(dto.getTags());
//        prompt.setTags(tagEntities);
//        promptRepository.save(prompt);
//
//        PromptClassification classification = PromptClassification.builder()
//                .prompt(prompt)
//                .modelCategory(modelCategoryRepository.findById(dto.getModelCategoryId())
//                        .orElseThrow(() -> new RuntimeException("Model Category not found")))
//                .typeCategory(typeCategoryRepository.findById(dto.getTypeCategoryId())
//                        .orElseThrow(() -> new RuntimeException("Type Category not found")))
//                .build();
//
//        promptClassificationRepository.save(classification);
//        return prompt.getPromptID();
//    }
//
//    private List<Tag> resolveTags(List<String> tagNames) {
//        return tagNames.stream()
//                .map(name -> tagRepository.findByName(name)
//                        .orElseGet(() -> tagRepository.save(new Tag(name))))
//                .collect(Collectors.toList());
//    }
//
//    // 프론프트 수정
//    @Transactional
//    public void updatePrompt(Long id, PromptUpdateRequestDto dto) {
//        Prompt prompt = promptRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Prompt not found"));
//
//        prompt.setPromptName(dto.getTitle());
//        prompt.setPromptContent(dto.getContent());
//        prompt.setPrice(dto.getPrice());
//        prompt.setUpdatedAt(LocalDateTime.now());
//
//        List<Tag> tagEntities = resolveTags(dto.getTags());
//        prompt.setTags(tagEntities);
//    }
//
//    // 프론프트 삭제
//    @Transactional
//    public void deletePrompt(Long id) {
//        Prompt prompt = promptRepository.findById(id) //todo: 유저ID 입력받아서 본인것인지 검증하는 메커니즘 필요
//                .orElseThrow(() -> new RuntimeException("Prompt not found"));
//        promptRepository.delete(prompt);
//    }
//}
