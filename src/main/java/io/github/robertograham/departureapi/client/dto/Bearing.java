package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

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

    private static final Map<String, Bearing> VALUE_LOOKUP = EnumValueLookupHelper.createValueEnumLookup(Bearing.class, Bearing::getValue);

    @Getter
    @NonNull
    private final String value;

    @JsonCreator
    public static Bearing fromValue(final String value) {
        return VALUE_LOOKUP.getOrDefault(value, null);
    }
}
