package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.*;
import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
import org.example.prumpt_be.dto.request.PromptUpdateRequestDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto; // 또는 상세 DTO 사용 가능
import org.example.prumpt_be.repository.*;
import org.example.prumpt_be.service.PromptCrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

// import java.time.LocalDateTime; // @UpdateTimestamp 사용 시 직접 설정 불필요
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        User owner = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id)); // TODO: 맞춤형 예외

        Prompt prompt = Prompt.builder()
                .promptName(createRequestDto.getPromptName())
                .promptContent(createRequestDto.getPromptContent())
                .price(createRequestDto.getPrice())
                .exampleContentUrl(createRequestDto.getExampleContentUrl())
                .model(createRequestDto.getModelInfo()) // 스키마 상 'model' 컬럼, DTO에서는 'modelInfo'로 가정
                .owner(owner)
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
    public PromptSummaryDto updatePrompt(String auth0Id, Long promptId, PromptUpdateRequestDto updateRequestDto) {
        User currentUser = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id));

        Prompt promptToUpdate = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("수정할 프롬프트를 찾을 수 없습니다. ID: " + promptId));

        // 프롬프트 소유자 확인
        if (!promptToUpdate.getOwner().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("이 프롬프트를 수정할 권한이 없습니다."); // TODO: 맞춤형 권한 예외 처리
        }

        // 프롬프트 기본 정보 업데이트
        promptToUpdate.setPromptName(updateRequestDto.getPromptName());
        promptToUpdate.setPromptContent(updateRequestDto.getPromptContent());
        promptToUpdate.setPrice(updateRequestDto.getPrice());
        promptToUpdate.setExampleContentUrl(updateRequestDto.getExampleContentUrl());
        promptToUpdate.setModel(updateRequestDto.getModelInfo());
        // updatedAt은 @UpdateTimestamp에 의해 자동 업데이트 됩니다.

        // 카테고리 정보 업데이트 (기존 분류 모두 삭제 후 새로 추가하는 방식)
        // 1. 기존 PromptClassification 정보 삭제
        //    promptToUpdate.getClassifications().clear(); // 이렇게 하면 orphanRemoval=true 일 경우 DB에서도 삭제됨
        //    또는 promptClassificationRepository.deleteByPrompt_PromptId(promptId); 와 같이 직접 삭제
        //    주의: clear() 후 save() 시 Hibernate의 동작 방식에 따라 문제가 생길 수 있으므로,
        //    명시적으로 repository를 통해 삭제하는 것이 더 안전할 수 있습니다.
        //    여기서는 컬렉션을 비우고 새 항목을 추가한 후 Prompt를 저장하는 방식을 사용합니다.
        //    (orphanRemoval=true가 Prompt 엔티티의 classifications에 설정되어 있어야 함)

        // 기존 연관관계 제거 (DB에서 직접 삭제가 아닌, 컬렉션에서만 제거 후 새롭게 추가)
        // DB에서 직접 삭제하려면 repository.deleteAll(promptToUpdate.getClassifications()) 또는 repository.deleteByPrompt(promptToUpdate) 필요
        promptClassificationRepository.deleteAll(promptToUpdate.getClassifications()); // 기존 연관된 PromptClassification 삭제
        promptToUpdate.getClassifications().clear(); // 컬렉션 비우기

        // 2. 새로운 PromptClassification 정보 추가
        if (updateRequestDto.getModelCategoryIds() != null && !updateRequestDto.getModelCategoryIds().isEmpty() &&
                updateRequestDto.getTypeCategoryIds() != null && !updateRequestDto.getTypeCategoryIds().isEmpty()) {

            Integer modelCategoryId = updateRequestDto.getModelCategoryIds().get(0); // 예시로 첫 번째 요소 사용
            Integer typeCategoryId = updateRequestDto.getTypeCategoryIds().get(0);   // 예시로 첫 번째 요소 사용

            ModelCategory modelCategory = modelCategoryRepository.findById(modelCategoryId)
                    .orElseThrow(() -> new RuntimeException("모델 카테고리를 찾을 수 없습니다. ID: " + modelCategoryId));
            TypeCategory typeCategory = typeCategoryRepository.findById(typeCategoryId)
                    .orElseThrow(() -> new RuntimeException("타입 카테고리를 찾을 수 없습니다. ID: " + typeCategoryId));

            PromptClassification newClassification = PromptClassification.builder()
                    .prompt(promptToUpdate)
                    .modelCategory(modelCategory)
                    .typeCategory(typeCategory)
                    .build();
            promptToUpdate.getClassifications().add(newClassification);
        }

        Prompt updatedPrompt = promptRepository.save(promptToUpdate);
        return convertToPromptSummaryDto(updatedPrompt);
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
        User currentUser = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id));

        Prompt promptToDelete = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("삭제할 프롬프트를 찾을 수 없습니다. ID: " + promptId));

        // 프롬프트 소유자 확인
        if (!promptToDelete.getOwner().getUserId().equals(currentUser.getUserId())) {
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
        String ownerName = (prompt.getOwner() != null && prompt.getOwner().getProfileName() != null)
                ? prompt.getOwner().getProfileName()
                : "Unknown"; // 또는 적절한 기본값 설정

        return PromptSummaryDto.builder()
                .promptId(prompt.getPromptId())
                .promptName(prompt.getPromptName())
                .price(prompt.getPrice())
                .ownerProfileName(ownerName)
                .thumbnailImageUrl(prompt.getExampleContentUrl())
                .aiInspectionRate(prompt.getAiInspectionRate())
                .createdAt(prompt.getCreatedAt())
                .build();
    }
}