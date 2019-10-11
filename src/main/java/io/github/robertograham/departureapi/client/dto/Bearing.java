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
public enum Bearing {

    NORTH("N"),

    NORTH_EAST("NE"),

    EAST("E"),

    SOUTH_EAST("SE"),

    SOUTH("S"),

    SOUTH_WEST("SW"),

    WEST("W"),

    NORTH_WEST("NW");

    private static final Map<String, Bearing> VALUE_LOOKUP = Arrays.stream(Bearing.values())
        .collect(Collectors.toMap(Bearing::getValue, Function.identity()));

    @Getter
    @NonNull
    private final String value;

    @JsonCreator
    public static Bearing fromValue(final String value) {
        return Optional.ofNullable(VALUE_LOOKUP.get(value))
            .orElseThrow(() -> new IllegalArgumentException(String.format("No mapping for value, \"%s\"", value)));
    }
}
