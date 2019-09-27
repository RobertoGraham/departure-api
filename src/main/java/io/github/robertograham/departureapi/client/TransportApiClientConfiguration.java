package io.github.robertograham.departureapi.client;

import feign.RequestInterceptor;
import io.github.robertograham.departureapi.client.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.stream.Collectors;

final class TransportApiClientConfiguration {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Bean
    private RequestInterceptor requestInterceptor(@Value("${transportApiClient.applicationId}") final String applicationId,
                                                  @Value("${transportApiClient.applicationKey}") final String applicationKey) {
        return (final var requestTemplate) -> {
            requestTemplate.query("app_id", applicationId);
            requestTemplate.query("app_key", applicationKey);
        };
    }

    @Bean
    private FeignFormatterRegistrar feignFormatterRegistrar() {
        return (final var formatterRegistry) -> {
            formatterRegistry.addConverter(Group.class, String.class, Group::getValue);
            formatterRegistry.addConverter(NextBuses.class, String.class, NextBuses::getValue);
            formatterRegistry.addConverter(Stops.class, String.class, Stops::getValue);
            formatterRegistry.addConverter(TypeSet.class, String.class, typeSet -> typeSet.getTypes().stream()
                .filter(Objects::nonNull)
                .map(Type::getValue)
                .collect(Collectors.joining(",")));
            formatterRegistry.addConverter(LocalDate.class, String.class, LOCAL_DATE_FORMATTER::format);
            formatterRegistry.addConverter(LocalTime.class, String.class, LOCAL_TIME_FORMATTER::format);
        };
    }
}