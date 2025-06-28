package org.example.prumpt_be.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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
                // 1. CSRF 비활성화 (API 서버이므로 세션 기반이 아니라 토큰 기반이라면 보통 비활성화)
                .csrf(AbstractHttpConfigurer::disable)
                // 2. 세션을 생성하지 않게 설정
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())

                // 3. 인가(authorization) 규칙 설정
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/prompts/**").permitAll() // "/api/public/**" 엔드포인트는 모두 허용
                        .requestMatchers("/api/monitoring/**").authenticated() // "/api/monitoring/**" 엔드포인트는 인증된 사용자만 접근 가능
                        .requestMatchers("/api/users/**").authenticated()
                        .requestMatchers("/api/success/**").authenticated()
                        .requestMatchers("/confirm/**").authenticated()
                        .requestMatchers("/api/reviews/**").authenticated()

                        // 그 외 API는 모두 허용
                        .anyRequest().permitAll()
                )
                // 4. JWT 기반 리소스 서버로 작동하도록 설정
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt // JwtDecoder가 application.yml/properties 설정을 통해 자동 구성됨
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