package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
import org.example.prumpt_be.dto.request.PromptUpdateRequestDto;
import org.example.prumpt_be.dto.request.PromptUploadRequestDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto;
import org.example.prumpt_be.service.PromptCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 프롬프트의 생성, 조회, 수정, 삭제(CRUD) 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/prompts")
@RequiredArgsConstructor
@Tag(name = "Prompt CRUD", description = "프롬프트 생성, 조회, 수정, 삭제 API")
public class PromptCrudController {

    private final PromptCrudService promptCrudService;

    @Operation(summary = "새 프롬프트 생성", description = "새로운 프롬프트를 등록합니다.")
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<PromptSummaryDto> createPrompt(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "프롬프트 제목", required = true) @RequestParam("promptName") String promptName,
            @Parameter(description = "프롬프트 내용", required = true) @RequestParam("promptContent") String promptContent,
            @Parameter(description = "가격", required = true) @RequestParam("price") Integer price,
            @Parameter(description = "프롬프트 설명", required = true) @RequestParam("description") String description,
            @Parameter(description = "예시 파일 (이미지, 영상 등)") @RequestParam(value = "exampleFile", required = false) MultipartFile exampleFile,
            @Parameter(description = "예시 타입", required = true) @RequestParam("exampleType") PromptUploadRequestDto.ExampleType exampleType,
            @Parameter(description = "모델 카테고리 ID", required = true) @RequestParam("modelCategoryIds") Integer modelCategoryIds,
            @Parameter(description = "타입 카테고리 ID", required = true) @RequestParam("typeCategoryIds") Integer typeCategoryIds
    ) {
        // 파라미터 값들을 로깅하여 multipart/form-data 파싱 확인
        log.info("=== Multipart Form Data 파싱 결과 ===");
        log.info("promptName: {}", promptName);
        log.info("promptContent: {}", promptContent);
        log.info("price: {}", price);
        log.info("description: {}", description);
        log.info("exampleFile: {} (size: {})", 
                exampleFile != null ? exampleFile.getOriginalFilename() : "null",
                exampleFile != null ? exampleFile.getSize() : 0);
        log.info("exampleType: {}", exampleType);
        log.info("modelCategoryIds: {}", modelCategoryIds);
        log.info("typeCategoryIds: {}", typeCategoryIds);
        log.info("=======================================");

        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();

        // DTO 수동 생성 및 값 설정
        PromptCreateRequestDto createRequestDto = new PromptCreateRequestDto();
        createRequestDto.setPromptName(promptName);
        createRequestDto.setPromptContent(promptContent);
        createRequestDto.setPrice(price);
        createRequestDto.setDescription(description);
        createRequestDto.setExampleFile(exampleFile);
        createRequestDto.setExampleType(exampleType);
        createRequestDto.setModelCategoryIds(modelCategoryIds);
        createRequestDto.setTypeCategoryIds(typeCategoryIds);

        PromptSummaryDto createdPrompt = promptCrudService.createPrompt(userAuth0Id, createRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrompt);
    }

    @Operation(summary = "새 프롬프트 생성 (JSON)", description = "파일 업로드 없이 JSON으로 새로운 프롬프트를 등록합니다.")
    @PostMapping(consumes = {"application/json"})
    public ResponseEntity<PromptSummaryDto> createPromptJson(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody PromptCreateRequestDto createRequestDto
    ) {

        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();

        PromptSummaryDto createdPrompt = promptCrudService.createPrompt(userAuth0Id, createRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrompt);
    }

    @Operation(summary = "프롬프트 상세 조회", description = "특정 프롬프트의 상세 정보를 조회합니다.")
    @GetMapping("/{promptId}")
    public ResponseEntity<PromptDetailDTO> getPromptDetails( // 상세 DTO로 변경 고려
            @Parameter(description = "조회할 프롬프트의 ID")
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long promptId) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        PromptDetailDTO promptDetails = promptCrudService.getPromptDetails(userAuth0Id, promptId);
        return ResponseEntity.ok(promptDetails);
    }

    @Operation(summary = "프롬프트 정보 수정", description = "기존 프롬프트의 정보를 수정합니다. (소유자만 가능)")
    @PutMapping("/{promptId}")
    public ResponseEntity<PromptSummaryDto> updatePrompt(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true)
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "수정할 프롬프트의 ID")
            @PathVariable Long promptId,
            @RequestBody PromptUpdateRequestDto updateRequestDto) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        PromptSummaryDto updatedPrompt = promptCrudService.updatePrompt(userAuth0Id, promptId, updateRequestDto);
        return ResponseEntity.ok(updatedPrompt);
    }

    @Operation(summary = "프롬프트 삭제", description = "특정 프롬프트를 삭제합니다. (소유자만 가능)")
    @DeleteMapping("/{promptId}")
    public ResponseEntity<Void> deletePrompt(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true)
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "삭제할 프롬프트의 ID")
            @PathVariable Long promptId) {
        // JWT로 유저 ID 조회
        String userAuth0Id = jwt.getSubject();
        promptCrudService.deletePrompt(userAuth0Id, promptId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}