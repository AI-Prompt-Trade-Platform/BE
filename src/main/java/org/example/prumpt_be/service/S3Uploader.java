// src/main/java/com/yourpackage/service/S3Uploader.java
package org.example.prumpt_be.service;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
public class S3Uploader {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    // Presigned URL 만료 시간 (기본: 24시간)
    @Value("${aws.s3.url-expire-hours:24}")
    private long urlExpireHours;

    public S3Uploader(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    /**
     * MultipartFile을 받아 S3에 업로드 후 퍼블릭 URL을 반환
     */
    public String upload(MultipartFile multipartFile) {
        String originalName = multipartFile.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String key = UUID.randomUUID().toString() + extension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    // public-read로 하면 바로 퍼블릭 접근 가능
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())
            );

            // 퍼블릭 버킷일 경우, 아래와 같이 즉시 접근 가능한 URL 반환
            return s3Client.utilities()
                    .getUrl(builder -> builder.bucket(bucketName).key(key))
                    .toString();

        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }


    //버킷이 private일 때, Presigned URL을 생성하고 반환
    public String generatePresignedUrl(String key) {
        // 실제 S3 GetObject 요청 구성
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Presign 요청 구성 (만료 시간 설정)
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(urlExpireHours))
                .getObjectRequest(getObjectRequest)
                .build();

        // Presigned URL 생성
        return s3Presigner.presignGetObject(presignRequest)
                .url()
                .toString();
    }
}
