# Multi-stage build for Clinica Microservice
FROM gradle:8.7-jdk21 AS build

WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .

# Build the application
RUN gradle build --no-daemon -x test

# Production stage
FROM eclipse-temurin:21-jre-jammy

# Install security updates and required packages
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y --no-install-recommends \
        curl \
        dumb-init && \
    rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd -r clinica && useradd -r -g clinica clinica

WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /home/gradle/src/build/libs/clinica-*.jar app.jar

# Change ownership to non-root user
RUN chown -R clinica:clinica /app
USER clinica

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:9091/actuator/health || exit 1

# Expose port
EXPOSE 9091

# Use dumb-init to handle signals properly
ENTRYPOINT ["dumb-init", "--"]
CMD ["java", \
     "-XX:+UseContainerSupport", \
     "-XX:MaxRAMPercentage=75.0", \
     "-Djava.security.egd=file:/dev/./urandom", \
     "-jar", \
     "app.jar"]