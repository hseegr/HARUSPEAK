# Base 이미지: Java 17 slim
FROM openjdk:17-jdk-slim

# 타임존 설정 추가
ENV TZ=Asia/Seoul

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 jar 복사
COPY ./build/libs/batch-server-0.0.1-SNAPSHOT.jar ./app.jar

# jar 실행 (JVM에 타임존 직접 지정)
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "app.jar"]
