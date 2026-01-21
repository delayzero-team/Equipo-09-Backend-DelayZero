# Stage 1: Build the application
FROM maven:3.8.8-eclipse-temurin-17 AS build
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jre-slim
WORKDIR /app
COPY --from=build /app/target/delayzero-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
