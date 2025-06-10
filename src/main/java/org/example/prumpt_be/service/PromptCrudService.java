package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.PromptDetailDTO;
import org.example.prumpt_be.dto.request.PromptCreateRequestDto;
import org.example.prumpt_be.dto.request.PromptUpdateRequestDto;
import org.example.prumpt_be.dto.response.PromptSummaryDto; // 또는 상세 DTO

// todo: 프롬프트CRUD 서비스 인터페이스
public interface PromptCrudService {

    /**
     * 새로운 프롬프트를 생성합니다.
     * @param auth0Id 프롬프트 생성자(소유자)의 Auth0 ID
     * @param createRequestDto 프롬프트 생성 요청 정보
     * @return 생성된 프롬프트의 요약 정보 (또는 상세 정보)
     */
    PromptSummaryDto createPrompt(String auth0Id, PromptCreateRequestDto createRequestDto);

    /**
     * 특정 프롬프트의 상세 정보를 조회합니다.
     * @param promptId 조회할 프롬프트 ID
     * @return 프롬프트 상세 정보 DTO (별도 정의 필요 시) 또는 PromptSummaryDto
     */
    PromptDetailDTO getPromptDetails(Long promptId); // 상세 DTO가 필요할 수 있음

    /**
     * 특정 프롬프트 정보를 수정합니다.
     * 소유자만 수정 가능해야 합니다.
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID (소유자 확인용)
     * @param promptId 수정할 프롬프트 ID
     * @param updateRequestDto 프롬프트 수정 요청 정보
     * @return 수정된 프롬프트의 요약 정보 (또는 상세 정보)
     */
    PromptSummaryDto updatePrompt(String auth0Id, Long promptId, PromptUpdateRequestDto updateRequestDto);

    /**
     * 특정 프롬프트를 삭제합니다.
     * 소유자만 삭제 가능해야 합니다.
     * @param auth0Id 현재 인증된 사용자의 Auth0 ID (소유자 확인용)
     * @param promptId 삭제할 프롬프트 ID
     */
    void deletePrompt(String auth0Id, Long promptId);
}