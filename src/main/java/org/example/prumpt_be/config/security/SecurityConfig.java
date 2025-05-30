package org.example.prumpt_be.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

//스프링부트 시큐리티 설정
//Auth0 - JWT 방식
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(
            HttpSecurity http,
            CustomJwtAuthenticationConverter customJwtAuthConverter,
            JwtDecoder jwtDecoder     // 아래에서 정의할 커스텀 디코더
    ) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/public/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder)
                                .jwtAuthenticationConverter(customJwtAuthConverter)
                        )
                );
        return http.build();
    }

    /**
     * JWT 검증 시 추가로 Audience 체크를 넣는 Decoder
     */
    @Bean
    JwtDecoder jwtDecoder(@Value("${auth0.issuerUri}") String issuerUri,
                          @Value("${auth0.audience}")  String audience) {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withIssuerLocation(issuerUri).build();

        // iss 검증 + exp 등 기본 검증
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        // aud 검증 커스텀
        OAuth2TokenValidator<Jwt> withAudience = token -> {
            if (token.getAudience().contains(audience)) {
                return OAuth2TokenValidatorResult.success();
            }
            OAuth2Error err = new OAuth2Error(
                    "invalid_token",
                    "The required audience is missing",
                    null
            );
            return OAuth2TokenValidatorResult.failure(err);
        };

        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience)
        );
        return decoder;
    }
}