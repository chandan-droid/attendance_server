# Step 1: Build stage
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app

# Copy everything (including gradlew)
COPY . .

# Give execute permission to gradlew
RUN chmod +x ./gradlew

# Build the JAR
RUN ./gradlew clean build -x test --no-daemon

# Step 2: Runtime stage
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copy the built JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
