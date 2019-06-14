package io.github.robertograham.busapi.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Builder(builderClassName = "Builder", builderMethodName = "newBuilder")
@Value
@JsonDeserialize(builder = BusStopDeparturesResponse.Builder.class)
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

    @lombok.Builder(builderClassName = "Builder", builderMethodName = "newBuilder")
    @Value
    @JsonDeserialize(builder = Departure.Builder.class)
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
        @NonNull
        String lineName;

        @JsonProperty("direction")
        @NonNull
        String direction;

        @JsonProperty("operator")
        @NonNull
        String operator;

        @JsonProperty("date")
        @NonNull
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
        @NonNull
        String dir;

        @JsonProperty("id")
        @NonNull
        String id;

        @JsonProperty("operator_name")
        @NonNull
        String operatorName;
    }
}
