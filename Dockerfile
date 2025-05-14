# Use a base image with Java 23 (matching the build environment)
FROM eclipse-temurin:23-jdk

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the target directory
COPY target/airline-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 9090

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]