# Use an official OpenJDK runtime as a base image
FROM eclipse-temurin:21-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the application JAR file into the container at /app
COPY ./app/build/libs/app.jar /app/app.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the application when the container launches
CMD ["java", "-jar", "app.jar"]