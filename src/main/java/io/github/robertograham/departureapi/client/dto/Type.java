package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

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

    private static final Map<String, Type> VALUE_LOOKUP = EnumValueLookupHelper.createValueEnumLookup(Type.class, Type::getValue);

    @Getter
    @NonNull
    private final String value;

    @JsonCreator
    public static Type fromValue(final String value) {
        return VALUE_LOOKUP.getOrDefault(value, null);
    }
}
