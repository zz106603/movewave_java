FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

# 2단계: 실행 스테이지 (최종 이미지 최소화)
FROM eclipse-temurin:21-jre
WORKDIR /app

# 빌드 결과물만 복사
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
