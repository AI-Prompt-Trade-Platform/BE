package org.example.prumpt_be.service;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
// ObjectCannedACL을 import 합니다.
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class S3Uploader {

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

    /**
     * MultipartFile을 S3에 업로드하고, CloudFront URL을 반환합니다.
     * 업로드된 파일은 공개(public-read)로 설정됩니다.
     */
    public String upload(MultipartFile multipartFile, String bucketName, String dirName) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String key = String.join("/", dirName, UUID.randomUUID().toString() + "." + extension);

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(multipartFile.getContentType())
                // --- 이 부분을 추가하여 파일을 공개로 설정합니다 ---
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        s3Client.putObject(
                putObjectRequest,
                RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())
        );

        return "https://" + cloudFrontDomain + "/" + key;
    }

    // ... (delete 및 다른 메서드는 그대로 유지)
    public String generatePresignedUrl(String bucketName, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
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

    public void delete(String fileUrl, String bucketName) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        try {
            String key = extractKeyFromUrl(fileUrl);

            if (key == null) {
                return;
            }

            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(this.defaultBucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);

        } catch (S3Exception e) {
            System.err.println("S3 파일 삭제 중 오류 발생: " + e.awsErrorDetails().errorMessage());
        }
    }

    private String extractKeyFromUrl(String fileUrl) {
        String prefix = "https://" + this.cloudFrontDomain + "/";
        if (fileUrl.startsWith(prefix)) {
            return fileUrl.substring(prefix.length());
        }
        return null;
    }
}