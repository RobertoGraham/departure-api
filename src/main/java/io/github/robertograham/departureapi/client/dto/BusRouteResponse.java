package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(value = {"id"}, allowSetters = true)
public record BusRouteResponse(@JsonProperty("request_time") ZonedDateTime requestTime, String operator,
                               @JsonProperty("operator_name") String operatorName, String line,
                               @JsonProperty("line_name") String lineName,
                               @JsonProperty("origin_atcocode") String originAtcoCode, String dir, String id,
                               List<Stop> stops) {

    public BusRouteResponse {
        Objects.requireNonNull(requestTime, "requestTime cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(operatorName, "operatorName cannot be null");
        Objects.requireNonNull(line, "line cannot be null");
        Objects.requireNonNull(lineName, "lineName cannot be null");
        Objects.requireNonNull(originAtcoCode, "originAtcoCode cannot be null");
        Objects.requireNonNull(dir, "dir cannot be null");
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(stops, "stops cannot be null");
    }

    public record Stop(LocalTime time, LocalDate date, @JsonProperty("atcocode") String atcoCode, String name,
                       @JsonProperty("stop_name") String stopName, @JsonProperty("smscode") String smsCode,
                       String locality, Bearing bearing, String indicator, BigDecimal latitude, BigDecimal longitude,
                       BusRouteResponse.Stop.Next next) {

        public Stop {
            Objects.requireNonNull(time, "time cannot be null");
            Objects.requireNonNull(date, "date cannot be null");
            Objects.requireNonNull(atcoCode, "atcoCode cannot be null");
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(stopName, "stopName cannot be null");
            Objects.requireNonNull(smsCode, "smsCode cannot be null");
            Objects.requireNonNull(locality, "locality cannot be null");
            Objects.requireNonNull(indicator, "indicator cannot be null");
            Objects.requireNonNull(latitude, "latitude cannot be null");
            Objects.requireNonNull(longitude, "longitude cannot be null");
        }

        public record Next(List<List<BigDecimal>> coordinates) {

            public Next {
                Objects.requireNonNull(coordinates, "coordinates cannot be null");
            }
        }
    }
}
