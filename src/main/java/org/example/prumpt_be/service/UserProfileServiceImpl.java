package org.example.prumpt_be.service;

import org.example.prumpt_be.dto.entity.Users;
import org.example.prumpt_be.dto.request.UserProfileUpdateDto;
import org.example.prumpt_be.dto.response.UserProfileDto;
import org.example.prumpt_be.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value; // 더 이상 필요하지 않으므로 제거합니다.
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.EntityNotFoundException;

import java.io.IOException;

/**
 * UserProfileService의 구현체입니다.
 * 사용자 프로필 정보 조회 및 수정 로직을 실제로 수행합니다.
 */
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private static final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    // S3Uploader가 버킷 이름을 관리하므로 이 필드는 더 이상 필요 없습니다.
    // @Value("${aws.s3.bucket.default}")
    // private String defaultBucketName;

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(String auth0Id) {
        Users user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with auth0id: " + auth0Id));
        return convertToUserProfileDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDto getCurrentUserProfile(String auth0Id) {
        Users user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with auth0Id: " + auth0Id));
        return convertToUserProfileDto(user);
    }

    @Override
    @Transactional
    public UserProfileDto updateCurrentUserProfile(String auth0Id, UserProfileUpdateDto updateDto) {
        log.info(">>> 프로필 업데이트 요청 수신. auth0Id: {}", auth0Id);
        // ... (기존 로깅 로직은 그대로 유지)

        Users user = userRepository.findByAuth0Id(auth0Id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with auth0Id: " + auth0Id));

        // 텍스트 기반 필드 업데이트
        if (updateDto.getProfileName() != null && !updateDto.getProfileName().isBlank()) {
            user.setProfileName(updateDto.getProfileName());
        }
        if (updateDto.getIntroduction() != null) {
            user.setIntroduction(updateDto.getIntroduction());
        }

        try {
            // 프로필 이미지 업데이트 처리
            // S3에 저장될 폴더 이름으로 "profile"을 전달합니다.
            String newProfileImgUrl = processImageUpdate(updateDto.getProfileImg(), user.getProfileImg_url(), "profile");
            user.setProfileImg_url(newProfileImgUrl);

            // 배너 이미지 업데이트 처리
            // S3에 저장될 폴더 이름으로 "banner"를 전달합니다.
            String newBannerImgUrl = processImageUpdate(updateDto.getBannerImg(), user.getBannerImg_url(), "banner");
            user.setBannerImg_url(newBannerImgUrl);
        } catch (IOException e) {
            throw new RuntimeException("이미지 파일 처리 중 오류가 발생했습니다.", e);
        }

        return convertToUserProfileDto(user);
    }


    /**
     * 이미지 업데이트를 통합 처리하는 헬퍼 메서드입니다.
     * 기존 이미지를 S3에서 삭제하고, 새 이미지를 업로드한 후 URL을 반환합니다.
     *
     * @param newFile    새로 업로드할 파일 (null이면 변경 없음, 비어있으면 삭제 요청)
     * @param currentUrl DB에 저장된 현재 이미지 URL
     * @param directory  S3에 저장할 디렉토리 경로 (e.g., "profile", "banner")
     * @return DB에 저장할 최종 이미지 URL
     * @throws IOException 파일 처리 중 발생할 수 있는 예외
     */
    private String processImageUpdate(MultipartFile newFile, String currentUrl, String directory) throws IOException {
        // 1. 업데이트 요청이 없으면(파일이 null), 기존 URL을 그대로 반환합니다.
        if (newFile == null) {
            return currentUrl;
        }

        // 2. 업데이트 요청이 있으면, 기존 이미지를 S3에서 삭제합니다.
        //    (currentUrl이 실제로 존재할 경우에만 실행)
        if (currentUrl != null && !currentUrl.isEmpty()) {
            // 수정된 delete 메서드 호출 (파라미터 1개)
            s3Uploader.delete(currentUrl);
        }

        // 3. 새 파일이 비어있으면(사용자가 '이미지 삭제'를 선택한 경우), null을 반환하여 DB의 URL을 비웁니다.
        if (newFile.isEmpty()) {
            return null;
        }

        // 4. 새 파일이 실제로 존재하면 S3에 업로드하고, 반환된 새 URL을 반환합니다.
        //    수정된 upload 메서드 호출 (파라미터 2개)
        return s3Uploader.upload(newFile, directory);
    }


    // --- Helper Methods ---
    private UserProfileDto convertToUserProfileDto(Users user) {
        return UserProfileDto.builder()
                .userId(user.getUserId())
                .profileName(user.getProfileName())
                .introduction(user.getIntroduction())
                .profileImgUrl(user.getProfileImg_url())
                .bannerImgUrl(user.getBannerImg_url())
                .point(user.getPoint())
                .build();
    }
}