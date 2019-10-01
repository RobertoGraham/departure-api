package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum Type {

    TRAIN_STATION("train_station"),

    BUS_STOP("bus_stop"),

    SETTLEMENT("settlement"),

    REGION("region"),

    STREET("street"),

    POINT_OF_INTEREST("poi"),

    POSTCODE("postcode"),

    UNKNOWN("");

    private static final Map<String, Type> VALUE_TO_TYPE_LOOKUP;

    static {
        VALUE_TO_TYPE_LOOKUP = EnumSet.complementOf(EnumSet.of(UNKNOWN)).stream()
            .collect(Collectors.toMap(Type::getValue, Function.identity()));
    }

    @Getter
    @NonNull
    private final String value;

    @JsonCreator
    public static Type fromValue(final String value) {
        return VALUE_TO_TYPE_LOOKUP.getOrDefault(value, UNKNOWN);
    }
}
