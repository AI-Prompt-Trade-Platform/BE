package org.example.prumpt_be.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "auth0")
public class Auth0Properties {
    /** 예: <a href="https://dev-q64r0n0blzhir6y0.us.auth0.com/">...</a> */
    private String issuerUri;
    /** 예: <a href="https://dev-q64r0n0blzhir6y0.us.auth0.com/api/v2/">...</a> */
    private String audience;
    public String getIssuerUri() { return issuerUri; }
    public void setIssuerUri(String issuerUri) { this.issuerUri = issuerUri; }
    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }
}