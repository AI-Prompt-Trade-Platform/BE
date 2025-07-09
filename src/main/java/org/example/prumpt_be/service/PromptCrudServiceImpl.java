package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.dto.entity.*;
import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
import org.example.prumpt_be.dto.request.PromptUpdateRequestDto;
import org.example.prumpt_be.dto.request.PromptUploadRequestDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto; // 또는 상세 DTO 사용 가능
import org.example.prumpt_be.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// import java.time.LocalDateTime; // @UpdateTimestamp 사용 시 직접 설정 불필요
import java.util.Objects;

/**
 * PromptCrudService 인터페이스의 구현체입니다.
 * 프롬프트의 생성(Create), 읽기(Read), 갱신(Update), 삭제(Delete) 관련
 * 실제 비즈니스 로직을 수행합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PromptCrudServiceImpl implements PromptCrudService {

    private final PromptRepository promptRepository;
    private final UserRepository userRepository;
    private final ModelCategoryRepository modelCategoryRepository;
    private final TypeCategoryRepository typeCategoryRepository;
    private final HomePageServiceImpl homePageService;
    private final AInspectionService inspectionService;

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
    @Transactional
    public PromptSummaryDto createPrompt(String auth0Id, PromptCreateRequestDto createRequestDto) {
        log.info("=== PromptCrudService.createPrompt 시작 ===");
        log.info("Auth0 ID: {}", auth0Id);
        log.info("DTO 내용 - promptName: {}, promptContent: {}, price: {}", 
                createRequestDto.getPromptName(), 
                createRequestDto.getPromptContent(), 
                createRequestDto.getPrice());
        
        Users owner = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. Auth0 ID: " + auth0Id));
        log.info("사용자 조회 성공: {}", owner.getUserId());

        Prompt prompt = Prompt.builder()
                .promptName(createRequestDto.getPromptName())
                .promptContent(createRequestDto.getPromptContent())
                .price(createRequestDto.getPrice())
                .description(createRequestDto.getDescription())
                .ownerID(owner)
                .build();
        
        log.info("Prompt 엔티티 생성 완료 - promptName: {}", prompt.getPromptName());


        // 2. PromptClassification 설정 (기존 로직 유지)
        if (createRequestDto.getModelCategoryIds() != null &&
                createRequestDto.getTypeCategoryIds() != null ) {
            // ... (카테고리 설정 로직) ...
            Integer modelCategoryId = createRequestDto.getModelCategoryIds();
            Integer typeCategoryId = createRequestDto.getTypeCategoryIds();

            ModelCategory modelCategory = modelCategoryRepository.findById(modelCategoryId)
                    .orElseThrow(() -> new RuntimeException("모델 카테고리를 찾을 수 없습니다. ID: " + modelCategoryId));
            TypeCategory typeCategory = typeCategoryRepository.findById(typeCategoryId)
                    .orElseThrow(() -> new RuntimeException("타입 카테고리를 찾을 수 없습니다. ID: " + typeCategoryId));

            PromptClassification classification = PromptClassification.builder()
                    .prompt(prompt)
                    .modelCategory(modelCategory)
                    .typeCategory(typeCategory)
                    .build();
            prompt.setClassifications(classification);
        }

        // 3. 프롬프트 초기 저장 (ID 생성을 위해)
        log.info("첫 번째 저장 시도 - promptName: {}", prompt.getPromptName());
        try {
            Prompt savedPrompt = promptRepository.save(prompt); //todo: 예외처리 메세지 반환하는 Service 필요
            log.info("첫 번째 저장 성공 - promptId: {}, promptName: {}", 
                    savedPrompt.getPromptId(), savedPrompt.getPromptName());

            // 4. AInspectionService를 통해 파일 업로드 및 AI 검수 처리
            // PromptUploadRequestDto는 AInspectionService가 필요로 하는 파일과 타입 정보를 전달합니다.
            PromptUploadRequestDto uploadRequest = new PromptUploadRequestDto();
            uploadRequest.setPromptId(savedPrompt.getPromptId());
            uploadRequest.setExampleFile(createRequestDto.getExampleFile()); // DTO에서 받은 파일
            uploadRequest.setExampleType(createRequestDto.getExampleType());   // DTO에서 받은 타입
            log.info("S3 업로드 및 AI 검수 시작");

            // AInspectionService는 내부적으로 S3에 파일을 업로드하고,
            // 해당 Prompt 엔티티의 exampleContentUrl 필드를 S3 URL로 업데이트 후 저장.
            // 또한 AI 검수 결과를 Prompt 엔티티에 업데이트하고 저장.
            inspectionService.handlePromptUploadAndEvaluation(uploadRequest);
            log.info("S3 업로드 및 AI 검수 완료");

            // 5. AInspectionService에 의해 변경된 최종 프롬프트 정보를 다시 조회
            Prompt finalPrompt = promptRepository.findById(savedPrompt.getPromptId())
                    .orElseThrow(() -> new RuntimeException("프롬프트를 다시 찾을 수 없습니다. ID: " + savedPrompt.getPromptId()));
            log.info("최종 프롬프트 조회 완료");

            return convertToPromptSummaryDto(finalPrompt);
        } catch (Exception e) {
            log.error("프롬프트 저장 중 오류 발생: {}", e.getMessage(), e);
            throw e;
        }
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
    public PromptDetailDTO getPromptDetails(String auth0Id, Long promptId) {
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new RuntimeException("프롬프트를 찾을 수 없습니다. ID: " + promptId));
        // 1. 프롬프트 상세 정보 DTO로 변환 + prompt 정보 주입
        PromptDetailDTO promptDetailDTO = convertToPromptDetailDTO(auth0Id,prompt);
        // 2. 구매 여부 확인
        boolean userPurchased = prompt.getPurchases().stream()
                // prompt.getPurchases() 는 이미 promptId 로 연관된 구매만 가져오므로 promptId 체크는 선택사항입니다.
                .anyMatch(purchase ->
                        purchase.getBuyer().getAuth0Id().equals(auth0Id)
                );

        promptDetailDTO.setUserPurchased(userPurchased);
        return promptDetailDTO;
    }

    // 프롬프트 업데이트
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
//        prompt.getClassifications().clear();
//        promptClassificationRepository.deleteByPrompt_PromptId(promptId);
        prompt.setClassifications(null);

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
                prompt.setClassifications(pc);
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
        if (!Objects.equals(promptToDelete.getOwnerID().getUserId(), currentUser.getUserId())) {
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

    private PromptDetailDTO convertToPromptDetailDTO(String auth0Id, Prompt prompt) {
        if (prompt == null) {
            return null;
        }
        return homePageService.getPromptDetailDto(auth0Id, prompt);
    }
}