package org.example.prumpt_be.config; // 패키지 경로는 프로젝트 구조에 맞게 조정하세요.

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class AwsS3Config {

    @Value("${aws.s3.region}") // application.properties 또는 application.yml 에 설정된 값
    private String region;

    @Value("${aws.credentials.access-key}") // application.properties 또는 application.yml 에 설정된 값
    private String accessKey;

    @Value("${aws.credentials.secret-key}") // application.properties 또는 application.yml 에 설정된 값
    private String secretKey;

    @Bean
    public S3Client s3Client() {
        // AWS 자격 증명 설정 방법은 다양합니다.
        // 여기서는 application.properties에서 직접 읽어오는 방식을 사용합니다.
        // EC2 인스턴스 프로파일, 환경 변수 등을 사용하는 것이 더 안전하고 권장됩니다.
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }
}