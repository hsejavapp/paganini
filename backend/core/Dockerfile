# Use a base image with Java installed
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/core-1.0-jar-with-dependencies.jar /app/your-app.jar

# Expose the port on which your application will run
EXPOSE 8080

# Set the command to run your application
CMD ["java", "-jar", "/app/your-app.jar"]
