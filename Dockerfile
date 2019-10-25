ARG SOURCES_DIR=/src

FROM maven:3.6.2-jdk-13 AS builder
ARG SOURCES_DIR
WORKDIR $SOURCES_DIR
COPY pom.xml .
RUN mvn -e -B dependency:go-offline
COPY src ./src
RUN mvn -e -B package

FROM azul/zulu-openjdk-alpine:13
ARG SOURCES_DIR
ENV PORT=8080
WORKDIR /app
COPY --from=builder $SOURCES_DIR/target/*.jar application.jar
EXPOSE $PORT
CMD [ "java", "-jar", "application.jar" ]