ARG SOURCES_DIR=/src

FROM azul/zulu-openjdk-alpine:17.0.5-17.38.21 AS builder
ARG SOURCES_DIR
WORKDIR $SOURCES_DIR
COPY build.gradle.kts .
COPY gradlew .
COPY settings.gradle.kts .
COPY src ./src
COPY gradle ./gradle
RUN ./gradlew bootJar

FROM azul/zulu-openjdk-alpine:17.0.5-17.38.21-jre-headless
ARG SOURCES_DIR
ENV PORT=8080
LABEL org.opencontainers.image.source=https://github.com/robertograham/departure-api
WORKDIR /app
COPY --from=builder $SOURCES_DIR/build/libs/*.jar application.jar
EXPOSE $PORT
CMD [ "java", "-jar", "application.jar" ]
