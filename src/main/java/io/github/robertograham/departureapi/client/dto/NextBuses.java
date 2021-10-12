package io.github.robertograham.departureapi.client.dto;

import java.util.Objects;

public enum NextBuses {

    YES("yes"),

    NO("no");

    private final String value;

    NextBuses(final String value) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public String getValue() {
        return value;
    }
}
