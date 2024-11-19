#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY build/libs/springSecurityMaster-0.0.1-SNAPSHOT.jar /app/myapp.jar
#
#ENTRYPOINT ["java", "-jar", "myapp.jar"]
# Build stage
FROM gradle:8.10-jdk17 AS build
WORKDIR /app
COPY . .
# 디버깅을 위한 ls 명령어 실행 (확인용)
RUN ls -al /app
RUN gradle build --no-daemon -x test

# Run stage
FROM --platform=linux/amd64 openjdk:17-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]