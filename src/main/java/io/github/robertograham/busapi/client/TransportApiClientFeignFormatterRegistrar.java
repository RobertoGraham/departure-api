package io.github.robertograham.busapi.client;

import io.github.robertograham.busapi.client.dto.Group;
import io.github.robertograham.busapi.client.dto.NextBuses;
import org.springframework.cloud.openfeign.FeignFormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
final class TransportApiClientFeignFormatterRegistrar implements FeignFormatterRegistrar {

    private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    public void registerFormatters(final FormatterRegistry formatterRegistry) {
        formatterRegistry.addConverter(Group.class, String.class, Group::getValue);
        formatterRegistry.addConverter(NextBuses.class, String.class, NextBuses::getValue);
        formatterRegistry.addConverter(LocalDate.class, String.class, LOCAL_DATE_FORMATTER::format);
        formatterRegistry.addConverter(LocalTime.class, String.class, LOCAL_TIME_FORMATTER::format);
    }
}