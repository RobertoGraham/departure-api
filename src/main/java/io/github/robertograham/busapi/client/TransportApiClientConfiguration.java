package io.github.robertograham.busapi.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.ErrorDecoder;
import io.github.robertograham.busapi.client.dto.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

final class TransportApiClientConfiguration {

    @Bean
    private RequestInterceptor requestInterceptor(@Value("${transportApiClient.applicationId}") final String applicationId,
                                                  @Value("${transportApiClient.applicationKey}") final String applicationKey) {
        return (final RequestTemplate requestTemplate) -> {
            requestTemplate.query("app_id", applicationId);
            requestTemplate.query("app_key", applicationKey);
        };
    }

    @Bean
    private FeignFormatterRegistrar feignFormatterRegistrar() {
        return (final FormatterRegistry formatterRegistry) -> {
            formatterRegistry.addConverter(Group.class, String.class, Group::getValue);
            formatterRegistry.addConverter(NextBuses.class, String.class, NextBuses::getValue);
            formatterRegistry.addConverter(Stops.class, String.class, Stops::getValue);
            formatterRegistry.addConverter(TypeSet.class, String.class, typeSet -> typeSet.getTypes().stream()
                    .filter(Objects::nonNull)
                    .map(Type::getValue)
                    .collect(Collectors.joining(",")));
            formatterRegistry.addConverter(LocalDate.class, String.class, DateTimeFormatter.ofPattern("yyyy-MM-dd")::format);
            formatterRegistry.addConverter(LocalTime.class, String.class, DateTimeFormatter.ofPattern("HH:mm")::format);
        };
    }

    @Bean
    private ErrorDecoder errorDecoder() {
        return (final String methodKey, final Response response) -> {
            final String responseBodyAsString = Optional.ofNullable(response.body())
                    .map((final Response.Body responseBody) -> {
                        try {
                            return responseBody.asInputStream();
                        } catch (final IOException exception) {
                            return null;
                        }
                    })
                    .map((final InputStream inputStream) -> {
                        try (inputStream) {
                            return IOUtils.toString(inputStream);
                        } catch (final IOException exception) {
                            return null;
                        }
                    })
                    .orElse(null);
            final ResponseStatusException responseStatusException = Optional.ofNullable(HttpStatus.resolve(response.status()))
                    .map(httpStatus -> new ResponseStatusException(httpStatus, responseBodyAsString))
                    .orElse(null);
            return responseStatusException != null ?
                    responseStatusException
                    : new ErrorDecoder.Default().decode(methodKey, response);
        };
    }
}