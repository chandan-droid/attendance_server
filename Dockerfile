# Use official OpenJDK image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy Gradle build files and source code
COPY src/main/java .

# Build the jar file
RUN ./gradlew clean build -x test

# Expose port 8080
EXPOSE 8080

# Run the Spring Boot app
CMD ["java", "-jar", "$(find build/libs -name '*.jar' | head -n 1)"]
