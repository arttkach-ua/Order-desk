# Use Eclipse Temurin (official OpenJDK replacement) - Alpine for minimal size
FROM eclipse-temurin:17-jre-alpine

# Install curl for health checks (Alpine uses apk instead of apt-get)
RUN apk add --no-cache curl

# Set working directory
WORKDIR /app

# Create a non-root user for running the application (Alpine Linux syntax)
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the built JAR file
COPY build/libs/*.jar app.jar

# Change ownership to the non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose port 8080
EXPOSE 8080

# Add health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application with optimized JVM settings
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
