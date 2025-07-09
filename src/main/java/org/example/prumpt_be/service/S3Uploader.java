package org.example.prumpt_be.service;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

// SLF4J 로거를 위한 import 추가
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class S3Uploader {

    // 로거 인스턴스 생성
    private static final Logger log = LoggerFactory.getLogger(S3Uploader.class);

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final long urlExpireHours;
    private final String cloudFrontDomain;
    private final String defaultBucketName;

    public S3Uploader(
            S3Client s3Client,
            S3Presigner s3Presigner,
            @Value("${aws.s3.url-expire-hours:24}") long urlExpireHours,
            @Value("${aws.cloudfront.domain}") String cloudFrontDomain,
            @Value("${aws.s3.bucket.default}") String defaultBucketName
    ) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
        this.urlExpireHours = urlExpireHours;
        this.cloudFrontDomain = cloudFrontDomain;
        this.defaultBucketName = defaultBucketName;
    }

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String key = String.join("/", dirName, UUID.randomUUID() + "." + extension);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.defaultBucketName)
                .key(key)
                .contentType(multipartFile.getContentType())
                // .acl(ObjectCannedACL.PUBLIC_READ) // <-- 이 라인을 제거했습니다.
                .build();

        try {
            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())
            );
            log.info("S3에 파일이 성공적으로 업로드되었습니다. Key: {}", key);
        } catch (SdkException e) {
            // 로거를 사용하여 상세한 오류 정보를 기록합니다.
            log.error("S3 파일 업로드 실패. Key: {}", key, e);
            // 트랜잭션 롤백 등을 보장하기 위해 예외를 다시 던져줍니다.
            throw new RuntimeException("S3 파일 업로드에 실패했습니다.", e);
        }

        return "https://" + cloudFrontDomain + "/" + key;
    }

    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        String key = extractKeyFromUrl(fileUrl);
        if (key == null) {
            log.warn("URL에서 S3 키를 추출할 수 없습니다: {}", fileUrl);
            return;
        }

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(this.defaultBucketName)
                .key(key)
                .build();

        try {
            s3Client.deleteObject(deleteObjectRequest);
            log.info("S3에서 파일이 성공적으로 삭제되었습니다. Key: {}", key);
        } catch (SdkException e) {
            // 로거를 사용하여 상세한 오류 정보를 기록합니다.
            log.error("S3 파일 삭제 실패. Key: {}", key, e);
            // 비즈니스 로직에 따라 이 예외를 다시 던질 수도 있습니다.
            // throw new RuntimeException("S3 파일 삭제에 실패했습니다.", e);
        }
    }

    // generatePresignedUrl, extractKeyFromUrl 메서드는 변경 없음
    public String generatePresignedUrl(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(this.defaultBucketName)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(urlExpireHours))
                .getObjectRequest(getObjectRequest)
                .build();

        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }

    public String extractKeyFromUrl(String fileUrl) {
        String prefix = "https://" + this.cloudFrontDomain + "/";
        if (fileUrl.startsWith(prefix)) {
            return fileUrl.substring(prefix.length());
        }
        return null;
    }
}