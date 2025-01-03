# Use a Debian/Ubuntu base image
FROM openjdk:20-jdk-slim

WORKDIR /app

# Install MySQL client
RUN apt-get update && apt-get install -y default-mysql-client

COPY target/todo-1.0-SNAPSHOT.jar /app/todo-app.jar
COPY src/main/resources/config.yml /app/config.yml

EXPOSE 8080 8081

ENTRYPOINT ["java", "-jar", "todo-app.jar", "server", "config.yml"]
