# Stage 1: Build stage
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn clean package

# Stage 2: Final stage
FROM openjdk:latest
WORKDIR /tmp
COPY --from=build /app/target/seMethods-0.1.0.2-jar-with-dependencies.jar /tmp/
ENTRYPOINT ["java", "-jar", "seMethods-0.1.0.2-jar-with-dependencies.jar"]

