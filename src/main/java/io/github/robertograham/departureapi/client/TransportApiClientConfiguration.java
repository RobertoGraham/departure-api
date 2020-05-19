package io.github.robertograham.departureapi.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import io.github.robertograham.departureapi.client.dto.Group;
import io.github.robertograham.departureapi.client.dto.NextBuses;
import io.github.robertograham.departureapi.client.dto.Stops;
import io.github.robertograham.departureapi.client.dto.Type;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

final class TransportApiClientConfiguration {

    private final Map<String, Collection<String>> queryMap;

    TransportApiClientConfiguration(@Value("${transportApiClient.applicationId}") final String applicationId,
                                    @Value("${transportApiClient.applicationKey}") final String applicationKey) {
        queryMap = Map.of("app_id", List.of(applicationId), "app_key", List.of(applicationKey));
    }

    @Bean
    private RequestInterceptor requestInterceptor() {
        return (final var requestTemplate) -> requestTemplate.queries(queryMap);
    }

    @Bean
    private FeignFormatterRegistrar feignFormatterRegistrar(final ObjectMapper objectMapper) {
        return (final var formatterRegistry) -> {
            formatterRegistry.addConverter(Group.class, String.class, Group::getValue);
            formatterRegistry.addConverter(NextBuses.class, String.class, NextBuses::getValue);
            formatterRegistry.addConverter(Stops.class, String.class, Stops::getValue);
            formatterRegistry.addConverter(Type[].class, String.class, types -> Arrays.stream(types)
                .filter(Objects::nonNull)
                .map(type -> {
                    try {
                        return objectMapper.writeValueAsString(type);
                    } catch (final JsonProcessingException exception) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(typeString -> StringUtils.unwrap(typeString, '"'))
                .collect(Collectors.joining(",")));
            formatterRegistry.addConverter(LocalDate.class, String.class, LocalDate::toString);
            formatterRegistry.addConverter(LocalTime.class, String.class, localTime -> localTime.truncatedTo(ChronoUnit.MINUTES)
                .toString());
        };
    }
}