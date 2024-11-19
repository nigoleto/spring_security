FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/springSecurityMaster-0.0.1-SNAPSHOT.jar /app/myapp.jar

ENTRYPOINT ["java", "-jar", "myapp.jar"]