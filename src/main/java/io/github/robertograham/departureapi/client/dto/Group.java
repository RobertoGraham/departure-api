package io.github.robertograham.departureapi.client.dto;

import java.util.Objects;

public enum Group {

    ROUTE("route"),

    NO("no");

    private final String value;

    Group(final String value) {
        this.value = Objects.requireNonNull(value, "value cannot be null");
    }

    public String getValue() {
        return value;
    }
}
