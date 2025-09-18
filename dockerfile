FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/Daewoo.jar Daewoo.jar
COPY src/main/resources/application-prod.yml ./application-prod.yml

ENTRYPOINT ["java", "-jar", "Daewoo.jar"]