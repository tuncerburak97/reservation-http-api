FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Copy config files
COPY config ./config

# Build application
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM openjdk:21-jre-slim

WORKDIR /app

# Copy jar file and config
COPY --from=0 /app/target/*.jar app.jar
COPY --from=0 /app/config ./config

# Environment variables
ENV SERVER_PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
ENV MONGODB_URI=mongodb://localhost:27017/reservation_db
ENV MONGODB_DATABASE=reservation_db

# Expose port
EXPOSE ${SERVER_PORT}

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.config.location=classpath:/application.properties,file:./config/application.yaml"] 