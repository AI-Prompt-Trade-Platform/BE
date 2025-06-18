package org.example.prumpt_be.service;

import java.io.IOException;
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

//todo: S3에 파일 업로드 서비스 (필수)
@Service
//@ConditionalOnProperty(name = "aws.s3.enabled", havingValue = "true")
public class S3Uploader {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    // 단일 버킷 이름 대신, 각 타입별 버킷 이름을 AInspectionService에서 주입받거나,
    // upload 메소드에서 직접 받도록 변경합니다.
    // @Value("${aws.s3.bucket}")
    // private String bucketName; // 이 필드는 더 이상 직접 사용하지 않음

    // Presigned URL 만료 시간 (기본: 24시간)
    @Value("${aws.s3.url-expire-hours:24}")
    private long urlExpireHours;

    public S3Uploader(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    /**
     * MultipartFile을 받아 지정된 버킷의 지정된 디렉토리(폴더)에 S3 업로드 후 퍼블릭 URL을 반환
     */
    public String upload(MultipartFile multipartFile, String bucketName, String dirName) throws IOException {
        String originalName = multipartFile.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        // 디렉토리 이름과 파일 이름을 결합하여 S3 키 생성
        // 디렉토리 이름 뒤에 '/'를 붙여 폴더 구조처럼 보이게 함
        String key = dirName + "/" + UUID.randomUUID() + extension;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName) // 파라미터로 받은 버킷 이름 사용
                .key(key)
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
    }


    //버킷이 private일 때, Presigned URL을 생성하고 반환
    // 이 메소드도 어떤 버킷의 key인지 명시하기 위해 bucketName 파라미터를 추가하는 것이 좋습니다.
    public String generatePresignedUrl(String bucketName, String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName) // 파라미터로 받은 버킷 이름 사용
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
}