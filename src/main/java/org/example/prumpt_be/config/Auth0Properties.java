package org.example.prumpt_be.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "auth0")
public class Auth0Properties {
    /** 예: https://dev-q64r0n0blzhir6y0.us.auth0.com/ */
    private String issuerUri;
    /** 예: https://dev-q64r0n0blzhir6y0.us.auth0.com/api/v2/ */
    private String audience;
    public String getIssuerUri() { return issuerUri; }
    public void setIssuerUri(String issuerUri) { this.issuerUri = issuerUri; }
    public String getAudience() { return audience; }
    public void setAudience(String audience) { this.audience = audience; }
}