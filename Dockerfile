ARG BUILD_DIR=/usr/src/app

FROM maven:3.6.1-jdk-11-slim
ARG BUILD_DIR
WORKDIR $BUILD_DIR
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src src
RUN mvn package

FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.3_7_openj9-0.14.3
ARG BUILD_DIR
ENV PORT=8080
WORKDIR /opt/app
COPY --from=0 $BUILD_DIR/target/*.jar application.jar
EXPOSE $PORT
CMD [ "java", "-jar", "application.jar" ]