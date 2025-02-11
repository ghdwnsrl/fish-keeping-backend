FROM gradle:8.10.0-jdk21 AS build
WORKDIR /app

# 라이브러리 설치에 필요한 파일만 복사
COPY build.gradle settings.gradle ./
RUN gradle dependencies --no-daemon

# 호스트 머신의 소스코드를 작업 디렉토리로 복사
COPY . /app
RUN gradle clean build --no-daemon

FROM openjdk:21-jdk-slim

WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-Duser.timezone=Asia/Seoul","-jar", "app.jar"]
