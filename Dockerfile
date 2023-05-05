FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ./target/main-1.0.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
ENTRYPOINT ["java","-jar","/app.jar"]