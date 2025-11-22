
FROM gradle:8.7-jdk21-alpine AS builder

WORKDIR /app

COPY gradle/ ./gradle
COPY gradle.properties settings.gradle.kts build.gradle.kts ./

RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build the Spring Boot fat/boot jar
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# JVM optimizations for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+ExitOnOutOfMemoryError"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
