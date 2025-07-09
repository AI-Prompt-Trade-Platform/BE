package org.example.prumpt_be.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "aws")
@Component
public class AwsProperties {
    
    private S3 s3 = new S3();
    private Credentials credentials = new Credentials();
    private String region;
    
    // getter, setter
    public S3 getS3() {
        return s3;
    }
    
    public void setS3(S3 s3) {
        this.s3 = s3;
    }
    
    public Credentials getCredentials() {
        return credentials;
    }
    
    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
    
    public String getRegion() {
        return region;
    }
    
    public void setRegion(String region) {
        this.region = region;
    }
    
    // 내부 클래스들
    public static class S3 {
        private boolean enabled = true;
        private String bucket;
        
        public boolean isEnabled() {
            return enabled;
        }
        
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
        
        public String getBucket() {
            return bucket;
        }
        
        public void setBucket(String bucket) {
            this.bucket = bucket;
        }
    }
    
    public static class Credentials {
        private String accessKey;
        private String secretKey;
        
        public String getAccessKey() {
            return accessKey;
        }
        
        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }
        
        public String getSecretKey() {
            return secretKey;
        }
        
        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }
    }
}