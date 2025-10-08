FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/Daewoo.jar Daewoo.jar
COPY src/main/resources/application-local.yml ./application-local.yml

ENTRYPOINT ["java", "-jar", "Daewoo.jar"]
