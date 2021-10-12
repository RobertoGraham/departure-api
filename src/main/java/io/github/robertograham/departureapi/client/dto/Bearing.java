package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Bearing {

    @JsonProperty("N")
    NORTH,

    @JsonProperty("NE")
    NORTH_EAST,

    @JsonProperty("E")
    EAST,

    @JsonProperty("SE")
    SOUTH_EAST,

    @JsonProperty("S")
    SOUTH,

    @JsonProperty("SW")
    SOUTH_WEST,

    @JsonProperty("W")
    WEST,

    @JsonProperty("NW")
    NORTH_WEST
}
