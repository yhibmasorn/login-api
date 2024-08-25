# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-ea-13-jdk-slim-bullseye

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the target directory into the container
COPY target/login-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
