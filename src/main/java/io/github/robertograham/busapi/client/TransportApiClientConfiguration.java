package io.github.robertograham.busapi.client;

import feign.RequestInterceptor;
import io.github.robertograham.busapi.client.dto.Group;
import io.github.robertograham.busapi.client.dto.NextBuses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@lombok.Value
class TransportApiClientConfiguration {

    @Bean
    private RequestInterceptor requestInterceptor(@Value("${transportApiClient.applicationId}") final String applicationId,
                                                  @Value("${transportApiClient.applicationKey}") final String applicationKey) {
        return requestTemplate -> {
            requestTemplate.query("app_id", applicationId);
            requestTemplate.query("app_key", applicationKey);
        };
    }

    @Bean
    private FeignFormatterRegistrar feignFormatterRegistrar() {
        return formatterRegistry -> {
            formatterRegistry.addConverter(Group.class, String.class, Group::getValue);
            formatterRegistry.addConverter(NextBuses.class, String.class, NextBuses::getValue);
            formatterRegistry.addConverter(LocalDate.class, String.class, DateTimeFormatter.ofPattern("yyyy-MM-dd")::format);
            formatterRegistry.addConverter(LocalTime.class, String.class, DateTimeFormatter.ofPattern("HH:mm")::format);
        };
    }
}