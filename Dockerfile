ARG SOURCES_DIR=/src

FROM maven:3.6.1-jdk-11-slim AS builder
ARG SOURCES_DIR
WORKDIR $SOURCES_DIR
COPY pom.xml .
RUN mvn -e -B dependency:go-offline
COPY src ./src
RUN mvn -e -B package -DskipTests

FROM adoptopenjdk/openjdk11-openj9:x86_64-alpine-jre-11.0.3_7_openj9-0.14.3
ARG SOURCES_DIR
ENV PORT=8080
WORKDIR /app
COPY --from=builder $SOURCES_DIR/target/*.jar application.jar
EXPOSE $PORT
CMD [ "java", "-jar", "application.jar" ]