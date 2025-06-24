package org.example.prumpt_be.config;

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

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    /**
     * 재사용 가능한 단일 AWS 자격증명 제공자(Credentials Provider) Bean을 생성합니다.
     * 이렇게 하면 다른 Bean에서 자격증명 생성 로직이 중복되는 것을 방지할 수 있습니다.
     * 참고: 운영 환경에서는 키를 직접 사용하는 것보다 IAM 역할(EC2의 경우 Instance Profile)을
     * 사용하는 것이 더 안전하며, Spring Boot가 이를 자동으로 감지할 수 있습니다.
     * @return AWS 서비스를 위한 정적 자격증명 제공자.
     */
    @Bean
    public StaticCredentialsProvider awsCredentialsProvider() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        return StaticCredentialsProvider.create(credentials);
    }

    /**
     * S3Client Bean을 설정하며, 공유된 자격증명 제공자를 주입받습니다.
     * @param credentialsProvider 공유된 StaticCredentialsProvider Bean.
     * @return 설정이 완료된 S3Client 인스턴스.
     */
    @Bean
    public S3Client s3Client(StaticCredentialsProvider credentialsProvider) {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider) // 주입받은 Bean 사용
                .build();
    }

    /**
     * S3Presigner Bean을 설정하며, 공유된 자격증명 제공자를 주입받습니다.
     * @param credentialsProvider 공유된 StaticCredentialsProvider Bean.
     * @return 설정이 완료된 S3Presigner 인스턴스.
     */
    @Bean
    public S3Presigner s3Presigner(StaticCredentialsProvider credentialsProvider) {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(credentialsProvider) // 주입받은 Bean 사용
                .build();
    }
}