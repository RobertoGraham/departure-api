package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record BusStopDeparturesResponse(@JsonProperty("atcocode") String atcoCode,
                                        @JsonProperty("smscode") String smsCode,
                                        @JsonProperty("request_time") ZonedDateTime requestTime, String name,
                                        @JsonProperty("stop_name") String stopName, Bearing bearing, String indicator,
                                        String locality, Map<String, List<Departure>> departures,
                                        io.github.robertograham.departureapi.client.dto.BusStopDeparturesResponse.Location location) {

    public BusStopDeparturesResponse {
        Objects.requireNonNull(atcoCode, "atcoCode cannot be null");
        Objects.requireNonNull(smsCode, "smsCode cannot be null");
        Objects.requireNonNull(requestTime, "requestTime cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(stopName, "stopName cannot be null");
        Objects.requireNonNull(indicator, "indicator cannot be null");
        Objects.requireNonNull(locality, "locality cannot be null");
        Objects.requireNonNull(departures, "departures cannot be null");
        Objects.requireNonNull(location, "location cannot be null");
    }

    @JsonIgnoreProperties(value = {"id"}, allowSetters = true)
    public record Departure(String mode, String line, @JsonProperty("line_name") String lineName, String direction,
                            String operator, LocalDate date,
                            @JsonProperty("expected_departure_date") LocalDate expectedDepartureDate,
                            @JsonProperty("aimed_departure_time") LocalTime aimedDepartureTime,
                            @JsonProperty("expected_departure_time") LocalTime expectedDepartureTime,
                            @JsonProperty("best_departure_estimate") LocalTime bestDepartureEstimate, String source,
                            String dir, String id, @JsonProperty("operator_name") String operatorName) {

        public Departure {
            Objects.requireNonNull(mode, "mode cannot be null");
            Objects.requireNonNull(line, "line cannot be null");
            Objects.requireNonNull(operator, "operator cannot be null");
            Objects.requireNonNull(bestDepartureEstimate, "bestDepartureEstimate cannot be null");
            Objects.requireNonNull(source, "source cannot be null");
        }
    }

    public record Location(String type, List<BigDecimal> coordinates) {

        public Location {
            Objects.requireNonNull(type, "type cannot be null");
            Objects.requireNonNull(coordinates, "coordinates cannot be null");
        }
    }
}
