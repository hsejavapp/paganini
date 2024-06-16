# Use a base image with Java installed
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/core-1.0.jar /app/your-app.jar
COPY src/main/resources /app/resources

# Expose the port on which your application will run
EXPOSE 8080

# Set the command to run your application
CMD ["java", "-jar", "/app/your-app.jar"]
