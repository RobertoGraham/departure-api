package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;

@Builder(builderMethodName = "newBuilder", builderClassName = "Builder")
@Value
@JsonDeserialize(builder = BusServiceResponse.Builder.class)
@JsonPOJOBuilder(withPrefix = "")
@JsonIgnoreProperties(value = {"id"}, allowSetters = true)
public class BusServiceResponse {

    @JsonProperty("id")
    @NonNull
    String id;

    @JsonProperty("operator")
    @NonNull
    Operator operator;

    @JsonProperty("line")
    @NonNull
    String line;

    @JsonProperty("line_name")
    @NonNull
    String lineName;

    @JsonProperty("directions")
    @NonNull
    List<Direction> directions;

    @JsonProperty("centroid")
    @NonNull
    Centroid centroid;

    @JsonProperty("source")
    @NonNull
    String source;

    @JsonProperty("acknowledgements")
    @NonNull
    String acknowledgements;

    @lombok.Builder(builderMethodName = "newBuilder", builderClassName = "Builder")
    @Value
    @JsonDeserialize(builder = Operator.Builder.class)
    @JsonPOJOBuilder(withPrefix = "")
    public static class Operator {

        @JsonProperty("code")
        @NonNull
        String code;

        @JsonProperty("name")
        @NonNull
        String name;
    }

    @lombok.Builder(builderMethodName = "newBuilder", builderClassName = "Builder")
    @Value
    @JsonDeserialize(builder = Direction.Builder.class)
    @JsonPOJOBuilder(withPrefix = "")
    public static class Direction {

        @JsonProperty("name")
        @NonNull
        String name;

        @JsonProperty("destination")
        @NonNull
        Destination destination;

        @lombok.Builder(builderMethodName = "newBuilder", builderClassName = "Builder")
        @Value
        @JsonDeserialize(builder = Destination.Builder.class)
        @JsonPOJOBuilder(withPrefix = "")
        public static class Destination {

            @JsonProperty("description")
            @NonNull
            String description;
        }
    }

    @lombok.Builder(builderMethodName = "newBuilder", builderClassName = "Builder")
    @Value
    @JsonDeserialize(builder = Centroid.Builder.class)
    @JsonPOJOBuilder(withPrefix = "")
    public static class Centroid {

        @JsonProperty("type")
        @NonNull
        String type;

        @JsonProperty("coordinates")
        @NonNull
        List<BigDecimal> coordinates;
    }
}
