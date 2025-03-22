# 1. OpenJDK 기반 이미지 사용
FROM openjdk:17

# 2. JAR 파일 복사
# 빌드된 JAR 파일을 컨테이너로 복사합니다.
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 3. 애플리케이션 실행 명령
ENTRYPOINT ["java", "-jar", "/app.jar"]

