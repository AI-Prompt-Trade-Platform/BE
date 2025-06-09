//package org.example.prumpt_be.config;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
// import org.springframework.security.oauth2.core.OAuth2TokenValidator;
// import org.springframework.security.oauth2.jwt.*;
// import org.springframework.security.web.SecurityFilterChain;
// 
// import java.util.List;
// 
// 
// //스프링부트 시큐리티 설정
// //Auth0 - JWT 방식
// @Configuration
// public class SecurityConfig {
// 
//     @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
//     private String issuerUri;
// 
//     @Bean
//     public JwtDecoder jwtDecoder() {
//         NimbusJwtDecoder decoder = NimbusJwtDecoder
//                 .withJwkSetUri(issuerUri + ".well-known/jwks.json")
//                 .build();
// 
//         // 1) issuer 검증
//         OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
//         // 2) audience 검증
//         OAuth2TokenValidator<Jwt> withAudience = new JwtClaimValidator<List<String>>(
//                 "aud",
//                 aud -> aud != null && aud.contains("https://api.prumpt.local")
//         );
// 
//         decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience));
//         return decoder;
//     }

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .csrf(csrf -> csrf.disable())
//                 .authorizeHttpRequests(authz -> authz
//                         .requestMatchers("/api/public/**").permitAll()
//                         .requestMatchers("/api/**").permitAll()
//                         .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
//                         .requestMatchers("/success", "/fail").permitAll()
//                         .anyRequest().authenticated()
//                 )
//                 .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
// 
//         return http.build();
//     }
// 
// }
