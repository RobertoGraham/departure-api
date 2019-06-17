package io.github.robertograham.busapi.client.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Type {

    TRAIN_STATION("train_station"),

    BUS_STOP("bus_stop"),

    SETTLEMENT("settlement"),

    REGION("region"),

    STREET("street"),

    POINT_OF_INTEREST("poi"),

    POSTCODE("postcode");

    @Getter
    @NonNull
    private final String value;
}
