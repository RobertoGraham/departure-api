package io.github.robertograham.departureapi.response;

import java.math.BigDecimal;
import java.util.Objects;

public record BusStop(String id, String name, String locality, BigDecimal latitude, BigDecimal longitude) {

    public BusStop {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
    }
}
