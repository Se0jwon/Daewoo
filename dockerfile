FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR file (adjust the path and name)
COPY build/libs/Daewoo.jar Daewoo.jar

# Set command to run the JAR
ENTRYPOINT ["java", "-jar", "Daewoo.jar"]