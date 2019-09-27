package io.github.robertograham.departureapi.client.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Stops {

    ALL("all"),

    ONWARD("onward");

    @Getter
    @NonNull
    private final String value;
}
