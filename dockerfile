FROM openjdk:21-jdk-slim

WORKDIR /app

COPY build/libs/Daewoo-0.0.1-SNAPSHOT.jar Daewoo-0.0.1-SNAPSHOT.jar
COPY src/main/resources/application-prod.yml ./application-prod.yml

ENTRYPOINT ["java", "-jar", "Daewoo-0.0.1-SNAPSHOT.jar"]