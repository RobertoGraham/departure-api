ARG SOURCES_DIR=/src

FROM maven:3.8.4-openjdk-17-slim AS builder
ARG SOURCES_DIR
WORKDIR $SOURCES_DIR
COPY pom.xml .
RUN mvn -e -B dependency:go-offline
COPY src ./src
RUN mvn -e -B package

FROM azul/zulu-openjdk-alpine:18.0.2.1-jre-headless
ARG SOURCES_DIR
ENV PORT=8080
LABEL org.opencontainers.image.source=https://github.com/robertograham/departure-api
WORKDIR /app
COPY --from=builder $SOURCES_DIR/target/*.jar application.jar
EXPOSE $PORT
CMD [ "java", "-jar", "application.jar" ]
