# Use OpenJDK 21 as base image
FROM openjdk:21-jdk

# Set working directory
WORKDIR /app

# Create MIB directory
RUN mkdir -p /app/mib

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy source code
COPY src src

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "target/belajar-spring-boot-0.0.1-SNAPSHOT.jar"] 