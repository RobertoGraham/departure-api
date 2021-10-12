package io.github.robertograham.departureapi.client.dto;

import java.util.Objects;

public enum Stops {

    ALL("all"),

    ONWARD("onward");

    private final String value;

    Stops(final String value) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public String getValue() {
        return value;
    }
}
