package org.example.prumpt_be.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "toss")
@Component
public class TossProperties {
    
    private String clientKey;
    private String secretKey; // 필요한 다른 프로퍼티들도 추가
    
    // getter, setter
    public String getClientKey() {
        return clientKey;
    }
    
    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }
    
    public String getSecretKey() {
        return secretKey;
    }
    
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}