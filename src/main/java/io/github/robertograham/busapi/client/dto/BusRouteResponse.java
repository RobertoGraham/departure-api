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

@Builder
@Value
@JsonDeserialize(builder = BusRouteResponse.BusRouteResponseBuilder.class)
@JsonPOJOBuilder(withPrefix = "")
@JsonIgnoreProperties(value = {"id"}, allowSetters = true)
public class BusRouteResponse {

    @JsonProperty("request_time")
    @NonNull
    ZonedDateTime requestTime;

    @JsonProperty("operator")
    @NonNull
    String operator;

    @JsonProperty("operator_name")
    @NonNull
    String operatorName;

    @JsonProperty("line")
    @NonNull
    String line;

    @JsonProperty("line_name")
    @NonNull
    String lineName;

    @JsonProperty("origin_atcocode")
    @NonNull
    String originAtcoCode;

    @JsonProperty("dir")
    @NonNull
    String dir;

    @JsonProperty("id")
    @NonNull
    String id;

    @JsonProperty("stops")
    @NonNull
    List<Stop> stops;

    @Builder
    @Value
    @JsonDeserialize(builder = Stop.StopBuilder.class)
    @JsonPOJOBuilder(withPrefix = "")
    public static class Stop {

        @JsonProperty("time")
        @NonNull
        LocalTime time;

        @JsonProperty("date")
        @NonNull
        LocalDate date;

        @JsonProperty("atcocode")
        @NonNull
        String atcoCode;

        @JsonProperty("name")
        @NonNull
        String name;

        @JsonProperty("stop_name")
        @NonNull
        String stopName;

        @JsonProperty("smscode")
        @NonNull
        String smsCode;

        @JsonProperty("locality")
        @NonNull
        String locality;

        @JsonProperty("bearing")
        @NonNull
        String bearing;

        @JsonProperty("indicator")
        @NonNull
        String indicator;

        @JsonProperty("latitude")
        @NonNull
        BigDecimal latitude;

        @JsonProperty("longitude")
        @NonNull
        BigDecimal longitude;

        @JsonProperty("next")
        Next next;

        @Builder
        @Value
        @JsonDeserialize(builder = Next.NextBuilder.class)
        @JsonPOJOBuilder(withPrefix = "")
        public static class Next {

            @JsonProperty("coordinates")
            @NonNull
            List<List<BigDecimal>> coordinates;
        }
    }
}
