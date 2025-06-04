package org.example.prumpt_be.controller;

import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
import org.example.prumpt_be.dto.request.PromptUpdateRequestDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto; // 상세 DTO 사용 고려
import org.example.prumpt_be.service.PromptCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.security.core.annotation.AuthenticationPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 프롬프트의 생성, 조회, 수정, 삭제(CRUD) 관련 API 엔드포인트를 제공하는 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/prompts")
@RequiredArgsConstructor
@Tag(name = "Prompt CRUD", description = "프롬프트 생성, 조회, 수정, 삭제 API")
public class PromptCrudController {

    private final PromptCrudService promptCrudService;

    // 임시로 Auth0 ID를 헤더에서 받는다고 가정.
    private static final String AUTH0_ID_HEADER = "X-Auth0-Id";

    @Operation(summary = "새 프롬프트 생성", description = "새로운 프롬프트를 등록합니다.")
    @PostMapping
    public ResponseEntity<PromptSummaryDto> createPrompt(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true) @RequestHeader(AUTH0_ID_HEADER) String auth0Id,
            @RequestBody PromptCreateRequestDto createRequestDto) {
        PromptSummaryDto createdPrompt = promptCrudService.createPrompt(auth0Id, createRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrompt);
    }

    @Operation(summary = "프롬프트 상세 조회", description = "특정 프롬프트의 상세 정보를 조회합니다.")
    @GetMapping("/{promptId}")
    public ResponseEntity<PromptSummaryDto> getPromptDetails( // 상세 DTO로 변경 고려
            @Parameter(description = "조회할 프롬프트의 ID") @PathVariable Long promptId) {
        PromptSummaryDto promptDetails = promptCrudService.getPromptDetails(promptId);
        return ResponseEntity.ok(promptDetails);
    }

    @Operation(summary = "프롬프트 정보 수정", description = "기존 프롬프트의 정보를 수정합니다. (소유자만 가능)")
    @PutMapping("/{promptId}")
    public ResponseEntity<PromptSummaryDto> updatePrompt(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true) @RequestHeader(AUTH0_ID_HEADER) String auth0Id,
            @Parameter(description = "수정할 프롬프트의 ID") @PathVariable Long promptId,
            @RequestBody PromptUpdateRequestDto updateRequestDto) {
        PromptSummaryDto updatedPrompt = promptCrudService.updatePrompt(auth0Id, promptId, updateRequestDto);
        return ResponseEntity.ok(updatedPrompt);
    }

    @Operation(summary = "프롬프트 삭제", description = "특정 프롬프트를 삭제합니다. (소유자만 가능)")
    @DeleteMapping("/{promptId}")
    public ResponseEntity<Void> deletePrompt(
            @Parameter(description = "인증된 사용자의 Auth0 ID", required = true) @RequestHeader(AUTH0_ID_HEADER) String auth0Id,
            @Parameter(description = "삭제할 프롬프트의 ID") @PathVariable Long promptId) {
        promptCrudService.deletePrompt(auth0Id, promptId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}