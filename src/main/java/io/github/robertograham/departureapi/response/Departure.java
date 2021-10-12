package io.github.robertograham.departureapi.response;

import java.util.Objects;

public record Departure(String line, String lineName, String operator, long epochSecond, String operatorName,
                        String destination, String direction) {

    public Departure {
        Objects.requireNonNull(line, "line cannot be null");
        Objects.requireNonNull(lineName, "lineName cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(operatorName, "operatorName cannot be null");
    }
}
