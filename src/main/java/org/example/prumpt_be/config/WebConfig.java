package org.example.prumpt_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS 설정 (Cross-Origin Resource Sharing)
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 우리의 모든 API 경로에 이 규칙을 적용합니다.
                .allowedOrigins("https://www.prumpt2.store") // 이 주소에서 오는 요청을 허용합니다.
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드들을 지정합니다.
                .allowedHeaders("*") // 모든 요청 헤더를 허용합니다.
                .allowCredentials(true) // 쿠키 등 자격 증명 정보를 허용합니다.
                .maxAge(3600); // pre-flight 요청의 캐시 시간을 1시간으로 설정합니다.
    }
}

