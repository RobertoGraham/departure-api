package io.github.robertograham.busapi.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Value
@JsonDeserialize(builder = BusStopDeparturesResponse.BusStopDeparturesResponseBuilder.class)
@JsonPOJOBuilder(withPrefix = "")
public class BusStopDeparturesResponse {

    @JsonProperty("atcocode")
    @NonNull
    String atcoCode;

    @JsonProperty("smscode")
    @NonNull
    String smsCode;

    @JsonProperty("request_time")
    @NonNull
    ZonedDateTime requestTime;

    @JsonProperty("name")
    @NonNull
    String name;

    @JsonProperty("stop_name")
    @NonNull
    String stopName;

    @JsonProperty("bearing")
    @NonNull
    String bearing;

    @JsonProperty("indicator")
    @NonNull
    String indicator;

    @JsonProperty("locality")
    @NonNull
    String locality;

    @JsonProperty("departures")
    @NonNull
    Map<String, List<Departure>> departures;

    @JsonProperty("location")
    @NonNull
    Location location;

    @Builder
    @Value
    @JsonDeserialize(builder = Departure.DepartureBuilder.class)
    @JsonPOJOBuilder(withPrefix = "")
    @JsonIgnoreProperties(value = {"id"}, allowSetters = true)
    public static class Departure {

        @JsonProperty("mode")
        @NonNull
        String mode;

        @JsonProperty("line")
        @NonNull
        String line;

        @JsonProperty("line_name")
        String lineName;

        @JsonProperty("direction")
        String direction;

        @JsonProperty("operator")
        @NonNull
        String operator;

        @JsonProperty("date")
        LocalDate date;

        @JsonProperty("expected_departure_date")
        LocalDate expectedDepartureDate;

        @JsonProperty("aimed_departure_time")
        LocalTime aimedDepartureTime;

        @JsonProperty("expected_departure_time")
        LocalTime expectedDepartureTime;

        @JsonProperty("best_departure_estimate")
        @NonNull
        LocalTime bestDepartureEstimate;

        @JsonProperty("source")
        @NonNull
        String source;

        @JsonProperty("dir")
        String dir;

        @JsonProperty("id")
        String id;

        @JsonProperty("operator_name")
        String operatorName;
    }

    @Builder
    @Value
    @JsonDeserialize(builder = Location.LocationBuilder.class)
    @JsonPOJOBuilder(withPrefix = "")
    public static class Location {

        @JsonProperty("type")
        @NonNull
        String type;

        @JsonProperty("coordinates")
        @NonNull
        List<BigDecimal> coordinates;
    }
}
