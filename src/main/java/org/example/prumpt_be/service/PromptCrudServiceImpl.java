package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.*;
import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
import org.example.prumpt_be.dto.request.PromptUpdateRequestDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto; // 또는 상세 DTO 사용 가능
import org.example.prumpt_be.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

// import java.time.LocalDateTime; // @UpdateTimestamp 사용 시 직접 설정 불필요
import java.util.ArrayList;
import java.util.Objects;

/**
 * PromptCrudService 인터페이스의 구현체입니다.
 * 프롬프트의 생성(Create), 읽기(Read), 갱신(Update), 삭제(Delete) 관련
 * 실제 비즈니스 로직을 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class PromptCrudServiceImpl implements PromptCrudService {

    private final PromptRepository promptRepository;
    private final UserRepository userRepository;
    private final ModelCategoryRepository modelCategoryRepository;
    private final TypeCategoryRepository typeCategoryRepository;
    private final PromptClassificationRepository promptClassificationRepository; // 수정 시 기존 분류 삭제 등에 사용

    /**
     * 새로운 프롬프트를 생성하고 데이터베이스에 저장합니다.
     * 프롬프트 생성 시 연관된 카테고리 정보(PromptClassification)도 함께 처리합니다.
     *
     * @param auth0Id 프롬프트 생성자(소유자)의 Auth0 ID
     * @param createRequestDto 프롬프트 생성에 필요한 데이터 (이름, 내용, 가격, 카테고리 ID 등)
     * @return 생성된 프롬프트의 요약 정보 DTO
     * @throws RuntimeException 사용자를 찾을 수 없거나, 카테고리를 찾을 수 없는 경우 발생 (적절한 예외 처리 권장)
     */
    @Override
    @Transactional // 데이터 변경이 있으므로 트랜잭션 적용
    public PromptSummaryDto createPrompt(String auth0Id, PromptCreateRequestDto createRequestDto) {
        Users owner = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id)); // TODO: 맞춤형 예외

        Prompt prompt = Prompt.builder()
                .promptName(createRequestDto.getPromptName())
                .promptContent(createRequestDto.getPromptContent())
                .price(createRequestDto.getPrice())
                .exampleContentUrl(createRequestDto.getExampleContentUrl())
                .model(createRequestDto.getModelInfo()) // 스키마 상 'model' 컬럼, DTO에서는 'modelInfo'로 가정
                .ownerID(owner)
                // aiInspectionRate는 초기에는 null이거나 기본값, 또는 별도 검수 프로세스를 통해 설정될 수 있습니다.
                // createdAt, updatedAt은 @CreationTimestamp, @UpdateTimestamp에 의해 자동 관리됩니다.
                .classifications(new ArrayList<>()) // ★ classifications 리스트 초기화
                .build();

        // 카테고리 매핑 처리
        // DTO의 modelCategoryIds와 typeCategoryIds를 어떻게 조합할지는 정책에 따라 달라질 수 있습니다.
        // 예시: modelCategoryIds의 각 ID와 typeCategoryIds의 각 ID를 조합하여 여러 PromptClassification을 만들거나,
        //       하나의 modelCategoryId에 여러 typeCategoryId를 매칭하거나 등.
        // 현재는 가장 간단한 형태인, DTO에 전달된 첫 번째 modelCategoryId와 첫 번째 typeCategoryId만 사용한다고 가정합니다.
        // 만약 여러 조합을 저장해야 한다면 이 부분을 반복문 등으로 수정해야 합니다.
        if (createRequestDto.getModelCategoryIds() != null && !createRequestDto.getModelCategoryIds().isEmpty() &&
                createRequestDto.getTypeCategoryIds() != null && !createRequestDto.getTypeCategoryIds().isEmpty()) {

            // 이 예제에서는 각 카테고리 ID 리스트의 첫 번째 요소만 사용한다고 가정합니다.
            // 실제 요구사항에 따라 여러 카테고리 조합을 처리하도록 로직 확장 필요.
            Integer modelCategoryId = createRequestDto.getModelCategoryIds().get(0);
            Integer typeCategoryId = createRequestDto.getTypeCategoryIds().get(0);

            ModelCategory modelCategory = modelCategoryRepository.findById(modelCategoryId)
                    .orElseThrow(() -> new RuntimeException("모델 카테고리를 찾을 수 없습니다. ID: " + modelCategoryId));
            TypeCategory typeCategory = typeCategoryRepository.findById(typeCategoryId)
                    .orElseThrow(() -> new RuntimeException("타입 카테고리를 찾을 수 없습니다. ID: " + typeCategoryId));

            PromptClassification classification = PromptClassification.builder()
                    .prompt(prompt) // ★ 아직 DB에 저장되지 않은 Prompt 객체를 설정
                    .modelCategory(modelCategory)
                    .typeCategory(typeCategory)
                    .build();

            prompt.getClassifications().add(classification); // ★ Prompt 객체의 컬렉션에 추가
        }

        // Prompt 엔티티 저장 시, CascadeType.ALL (또는 PERSIST) 설정에 의해
        // prompt.classifications 리스트에 포함된 PromptClassification 객체들도 함께 저장됩니다.
        Prompt savedPrompt = promptRepository.save(prompt);

        return convertToPromptSummaryDto(savedPrompt);
    }

    /**
     * 특정 ID의 프롬프트 상세 정보를 조회합니다.
     *
     * @param promptId 조회할 프롬프트의 ID
     * @return 프롬프트 요약 정보 DTO (요구사항에 따라 상세 정보 DTO로 변경 가능)
     * @throws RuntimeException 프롬프트를 찾을 수 없는 경우 발생
     */
    @Override
    @Transactional(readOnly = true)
    public PromptSummaryDto getPromptDetails(Long promptId) {
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("프롬프트를 찾을 수 없습니다. ID: " + promptId));
        // 필요하다면 PromptSummaryDto 대신 더 상세한 정보를 담는 DTO를 만들어 반환할 수 있습니다.
        return convertToPromptSummaryDto(prompt);
    }

    /**
     * 특정 프롬프트의 정보를 수정합니다.
     * 프롬프트 소유자만 수정할 수 있도록 인증 로직이 필요합니다 (현재는 auth0Id로 소유자 확인).
     * 카테고리 정보도 함께 수정합니다 (기존 분류 삭제 후 새로 추가하는 방식).
     *
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID (프롬프트 소유자 확인용)
     * @param promptId 수정할 프롬프트의 ID
     * @param updateRequestDto 프롬프트 수정에 필요한 데이터
     * @return 수정된 프롬프트의 요약 정보 DTO
     * @throws RuntimeException 관련 엔티티를 찾을 수 없거나, 수정 권한이 없는 경우 발생
     */
    @Override
    @Transactional
    public PromptSummaryDto updatePrompt(
            String auth0Id,
            Long promptId,
            PromptUpdateRequestDto dto
    ) {
        Users currentUser = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("프롬프트를 찾을 수 없습니다."));

        // 소유자 검증
        if (!prompt.getOwnerID().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("수정 권한이 없습니다.");
        }

        // 1) 기본 정보 업데이트
        prompt.setPromptName(dto.getPromptName());
        prompt.setPromptContent(dto.getPromptContent());
        prompt.setPrice(dto.getPrice());
        prompt.setExampleContentUrl(dto.getExampleContentUrl());
        prompt.setModel(dto.getModelInfo());
        // updatedAt 은 @UpdateTimestamp 자동 반영

        // 2) classifications 전부 지우기 → orphanRemoval로 DB에서도 삭제됨
        prompt.getClassifications().clear();

        // 3) 새 PromptClassification 추가
        for (Integer modelCatId : dto.getModelCategoryIds()) {
            ModelCategory mc = modelCategoryRepository.findById(modelCatId)
                    .orElseThrow(() -> new RuntimeException("모델 카테고리 없음: " + modelCatId));
            for (Integer typeCatId : dto.getTypeCategoryIds()) {
                TypeCategory tc = typeCategoryRepository.findById(typeCatId)
                        .orElseThrow(() -> new RuntimeException("타입 카테고리 없음: " + typeCatId));

                PromptClassification pc = PromptClassification.builder()
                        .prompt(prompt)
                        .modelCategory(mc)
                        .typeCategory(tc)
                        .build();
                prompt.getClassifications().add(pc);
            }
        }

        // 4) 한 번에 save → cascade로 classification 삭제·삽입 모두 처리
        Prompt saved = promptRepository.save(prompt);
        return convertToPromptSummaryDto(saved);
    }


    /**
     * 특정 ID의 프롬프트를 삭제합니다.
     * 프롬프트 소유자만 삭제할 수 있도록 인증 로직이 필요합니다.
     * 연관된 PromptClassification, PromptPurchase, UserWishlist, PromptReview 등도 함께 삭제되거나
     * 정책에 따라 적절히 처리되어야 합니다 (DB Cascade 설정 또는 서비스 로직에서 명시적 처리).
     * 현재는 Prompt 엔티티의 연관관계에 CascadeType.ALL, orphanRemoval=true 등이 설정된 것을 가정합니다.
     *
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID (프롬프트 소유자 확인용)
     * @param promptId 삭제할 프롬프트의 ID
     * @throws RuntimeException 관련 엔티티를 찾을 수 없거나, 삭제 권한이 없는 경우 발생
     */
    @Override
    @Transactional
    public void deletePrompt(String auth0Id, Long promptId) {
        Users currentUser = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id));

        Prompt promptToDelete = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("삭제할 프롬프트를 찾을 수 없습니다. ID: " + promptId));

        // 프롬프트 소유자 확인
        if (promptToDelete.getOwnerID().getUserId() != (currentUser.getUserId())) {
            throw new RuntimeException("이 프롬프트를 삭제할 권한이 없습니다.");
        }

        // Prompt 엔티티를 삭제합니다.
        // @OneToMany 관계에 cascade = CascadeType.ALL, orphanRemoval = true 등이 설정되어 있다면,
        // 연관된 PromptClassification, PromptPurchase, UserWishlist, PromptReview 등도 함께 삭제됩니다.
        // 만약 UserSalesSummary 같은 테이블은 별도의 로직으로 업데이트(예: 판매 건수 감소 등)가 필요할 수 있습니다.
        promptRepository.delete(promptToDelete);
    }

    // --- Helper Methods ---
    // Prompt 엔티티를 PromptSummaryDto로 변환합니다.
    // (HomePageServiceImpl의 것과 중복되므로, 공통 유틸리티 클래스로 분리하는 것을 고려해볼 수 있습니다.)
    private PromptSummaryDto convertToPromptSummaryDto(Prompt prompt) {
        if (prompt == null) {
            return null;
        }
        return HomePageServiceImpl.getPromptSummaryDto(prompt);
    }
}