// /Users/madecafe/workSpace/Prumpt_2nd_Prj/Prumpt_BE/src/main/java/org/example/prumpt_be/service/AInspectionService.java
package org.example.prumpt_be.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.prumpt_be.dto.entity.Prompt;
import org.example.prumpt_be.dto.request.PromptUploadRequestDto;
import org.example.prumpt_be.dto.request.PromptUploadRequestDto.ExampleType;
import org.example.prumpt_be.repository.PromptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AInspectionService {

    private final S3Uploader s3Uploader;
    private final OpenAiService openAiService;
    private final PromptRepository promptRepository;

    /**
     * 프롬프트 생성 요청을 받아 파일 업로드 및 AI 평가를 총괄하는 메인 메서드입니다.
     * @param request 프롬프트 ID, 파일, 타입 정보가 담긴 DTO
     */
    @Transactional
    public void handlePromptUploadAndEvaluation(PromptUploadRequestDto request) {
        // 1. DB에서 프롬프트 엔티티 조회
        Long promptId = request.getPromptId();
        Prompt prompt = promptRepository.findById(promptId)
                .orElseThrow(() -> new IllegalArgumentException("Prompt not found with id: " + promptId));

        MultipartFile exampleFile = request.getExampleFile();
        ExampleType exampleType = request.getExampleType();

        // 2. 파일 타입에 따라 AI 평가 수행
        if (exampleFile != null && !exampleFile.isEmpty()) {
            // 2-1. 파일이 있는 경우 (이미지, 비디오)
            handleFileUploadAndEvaluation(prompt, exampleFile, exampleType);
        } else if (exampleType == ExampleType.TEXT) {
            // 2-2. 텍스트 예시만 있는 경우
            handleTextEvaluation(prompt);
        }

        // 3. 변경된 프롬프트 정보 최종 저장
        promptRepository.save(prompt);
    }

    /**
     * 파일(이미지/비디오) 업로드 및 AI 평가를 처리합니다.
     */
    private void handleFileUploadAndEvaluation(Prompt prompt, MultipartFile file, ExampleType type) {
        String objectKey;
        try {
            String dirName = determineDirName(type);
            // S3에 파일을 업로드하고, CloudFront URL을 받습니다.
            String cloudFrontUrl = s3Uploader.upload(file, dirName);
            // DB에는 사용자에게 보여줄 최종 URL을 저장합니다.
            prompt.setExampleContentUrl(cloudFrontUrl);
            // URL에서 S3 객체 키를 추출합니다.
            objectKey = s3Uploader.extractKeyFromUrl(cloudFrontUrl);
            if (objectKey == null) {
                throw new RuntimeException("S3 URL에서 객체 키를 추출할 수 없습니다: " + cloudFrontUrl);
            }
        } catch (IOException e) {
            throw new RuntimeException("S3 파일 업로드 중 오류가 발생했습니다.", e);
        }

        // OpenAI 평가를 위해 Pre-signed URL 생성
        String presignedUrl = s3Uploader.generatePresignedUrl(objectKey);
        log.info("생성된 Pre-signed URL: {}", presignedUrl);

        String inspectionResult;
        if (type == ExampleType.IMAGE) {
            // OpenAiService에 Pre-signed URL을 전달하여 평가 요청
            inspectionResult = openAiService.getInspectionRateForImageUrl(prompt.getPromptContent(), presignedUrl);
        } else {
            // 비디오 또는 다른 파일 타입도 URL 기반으로 평가 (OpenAI 정책에 따라 달라질 수 있음)
            String promptForAi = makePromptForEtc(prompt.getPromptContent(), presignedUrl);
            inspectionResult = openAiService.getInspectionRate(promptForAi);
        }

        prompt.setAiInspectionRate(inspectionResult);
    }

    /**
     * 텍스트 예시의 AI 평가를 처리합니다.
     */
    private void handleTextEvaluation(Prompt prompt) {
        // 텍스트 예시는 prompt 엔티티의 exampleContentUrl 필드에 저장되어 있다고 가정합니다.
        String promptForAi = makePromptForText(prompt.getPromptContent(), prompt.getExampleContentUrl());
        String inspectionResult = openAiService.getInspectionRate(promptForAi);
        prompt.setAiInspectionRate(inspectionResult);
    }

    /**
     * 파일 타입에 따라 S3 폴더 이름을 결정합니다.
     */
    private String determineDirName(ExampleType type) {
        return switch (type) {
            case IMAGE -> "prompts/image";
            case VIDEO -> "prompts/video";
            default -> throw new IllegalArgumentException("지원하지 않는 파일 타입입니다: " + type);
        };
    }

    // 헬퍼 메서드 (필요에 따라 OpenAiService로 이동 가능)
    private String makePromptForText(String promptBody, String exampleText) {
        // 텍스트 평가용 프롬프트 구성
        return String.format("프롬프트: %s\n텍스트 예시: %s\n\n위 프롬프트와 텍스트 예시의 유사도를 평가해주세요.", promptBody, exampleText);
    }

    private String makePromptForEtc(String promptBody, String fileUrl) {
        // 비디오 등 기타 파일 평가용 프롬프트 구성
        return String.format("프롬프트: %s\n파일 URL: %s\n\n위 프롬프트와 파일의 내용을 보고 유사도를 평가해주세요.", promptBody, fileUrl);
    }
}