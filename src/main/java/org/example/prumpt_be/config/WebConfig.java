package org.example.prumpt_be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// CORS 설정 (Cross-Origin Resource Sharing)
// AWS 구조상 필요는 없지만 혹시나 도메인 바뀔 가능성 고려해서 작성해둠
@Configuration
public class WebConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000")  // 허용할 프론트 URL  todo: (실제 운영할때에는 반드시 React서버의 도메인주소만 적용해야함!!!!!!!)
                    .allowedOrigins("http://localhost:8080")
                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods("GET","POST","PUT","DELETE")
                    .allowCredentials(true);                  // 쿠키 전송 허용
        }
}
