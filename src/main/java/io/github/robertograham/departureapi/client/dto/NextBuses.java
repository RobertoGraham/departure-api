package io.github.robertograham.departureapi.client.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NextBuses {

    YES("yes"),

    NO("no");

    @Getter
    @NonNull
    private final String value;
}
