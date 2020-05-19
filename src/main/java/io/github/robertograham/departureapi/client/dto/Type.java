package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Type {

    @JsonProperty("train_station")
    TRAIN_STATION,

    @JsonProperty("tube_station")
    TUBE_STATION,

    @JsonProperty("bus_stop")
    BUS_STOP,

    @JsonProperty("settlement")
    SETTLEMENT,

    @JsonProperty("region")
    REGION,

    @JsonProperty("street")
    STREET,

    @JsonProperty("poi")
    POINT_OF_INTEREST,

    @JsonProperty("postcode")
    POSTCODE
}
