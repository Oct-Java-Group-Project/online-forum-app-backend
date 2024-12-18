# Use OpenJDK as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/api-gateway.jar .

# Expose the API Gateway port
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "api-gateway.jar"]