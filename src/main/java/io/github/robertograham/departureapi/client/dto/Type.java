package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Type {

    TRAIN_STATION("train_station"),

    TUBE_STATION("tube_station"),

    BUS_STOP("bus_stop"),

    SETTLEMENT("settlement"),

    REGION("region"),

    STREET("street"),

    POINT_OF_INTEREST("poi"),

    POSTCODE("postcode");

    private static final Map<String, Type> VALUE_LOOKUP = Arrays.stream(Type.values())
        .collect(Collectors.toMap(Type::getValue, Function.identity()));

    @Getter
    @NonNull
    private final String value;

    @JsonCreator
    public static Type fromValue(final String value) {
        return Optional.ofNullable(VALUE_LOOKUP.get(value))
            .orElseThrow(() -> new IllegalArgumentException(String.format("No mapping for value, \"%s\"", value)));
    }
}
