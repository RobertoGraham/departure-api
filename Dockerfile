ARG BUILD_DIR=/usr/src/app

FROM maven:3.6.1-jdk-11-slim AS build
ARG BUILD_DIR
WORKDIR $BUILD_DIR
COPY pom.xml .
RUN mvn -B dependency:resolve dependency:resolve-plugins
COPY src src
RUN mvn package

FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.3_7
ARG BUILD_DIR
WORKDIR /opt/app
COPY --from=build $BUILD_DIR/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "app.jar" ]