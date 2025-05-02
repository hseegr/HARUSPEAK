# Base 이미지: Java 17 slim
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 jar 복사 (현재 경로 기준)
COPY ./build/libs/api-server-0.0.1-SNAPSHOT.jar ./app.jar

# jar 실행
ENTRYPOINT ["java", "-jar", "app.jar"]