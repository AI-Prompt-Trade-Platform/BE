# =================================================================
# Stage 1: Java17 corretto JDK 로 빌드하는 단계
# =================================================================
# Java 17 Amazon Corretto JDK를 사용하여 Gradle로 애플리케이션을 빌드합니다.
FROM amazoncorretto:17-alpine-jdk AS builder

# 작업 디렉토리를 설정합니다.
WORKDIR /app

# 소스코드 전체를 복사하기 전, 빌드설정 파일 먼저 복사 후 의존성 다운로드 => 코드변경시에도 매번 의존성 설치 불필요
COPY gradlew .
COPY gradle gradle

# Copy the build configuration files (Groovy DSL)
COPY build.gradle .
COPY settings.gradle .

# Grant execute permission to the gradlew script
RUN chmod +x ./gradlew

# Download dependencies first to leverage Docker cache
# This layer is only rebuilt when build.gradle changes
RUN ./gradlew dependencies

# Copy the rest of the application source code
COPY src src

# Build the application, creating the executable JAR
# The --no-daemon flag is recommended for CI/CD environments
RUN ./gradlew bootJar --no-daemon


# =================================================================
# Stage 2: 최종 이미지 생성 단계
# =================================================================
# JDK를 사용하여 애플리케이션을 실행합니다. (호환성을 위해)
FROM amazoncorretto:17-alpine-jdk

# Set the working directory
WORKDIR /app

# HEALTHCHECK에 필요한 curl 패키지를 설치합니다.
RUN apk add --no-cache curl

# 보안을 위해 appuser 라는 비루트 사용자를 생성합니다.
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Stage 1에서 빌드된 JAR 파일을 복사합니다.
COPY --from=builder /app/build/libs/*.jar app.jar

# 파일 소유권을 appuser로 변경합니다.
RUN chown appuser:appgroup app.jar

# 비루트 사용자로 실행합니다.
USER appuser

# Expose the port the application will run on
EXPOSE 8080

# Spring Boot Actuator의 health check 엔드포인트를 사용한 상태 확인
HEALTHCHECK --interval=30s --timeout=5s --start-period=15s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Define the command to run the application when the container starts
# 컨테이너 환경에 최적화된 JVM 옵션을 사용합니다.
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]