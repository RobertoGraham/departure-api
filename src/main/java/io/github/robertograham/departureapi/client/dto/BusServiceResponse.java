package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(value = {"id"}, allowSetters = true)
public record BusServiceResponse(String id,
                                 io.github.robertograham.departureapi.client.dto.BusServiceResponse.Operator operator,
                                 String line, @JsonProperty("line_name") String lineName, List<Direction> directions,
                                 io.github.robertograham.departureapi.client.dto.BusServiceResponse.Centroid centroid,
                                 String source, String acknowledgements) {

    public BusServiceResponse {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(operator, "operator cannot be null");
        Objects.requireNonNull(line, "line cannot be null");
        Objects.requireNonNull(lineName, "lineName cannot be null");
        Objects.requireNonNull(directions, "directions cannot be null");
        Objects.requireNonNull(centroid, "centroid cannot be null");
        Objects.requireNonNull(source, "source cannot be null");
        Objects.requireNonNull(acknowledgements, "acknowledgements cannot be null");
    }

    public record Operator(String code, String name) {

        public Operator {
            Objects.requireNonNull(code, "code cannot be null");
            Objects.requireNonNull(name, "name cannot be null");
        }
    }

    public record Direction(String name, BusServiceResponse.Direction.Destination destination) {

        public Direction {
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(destination, "destination cannot be null");
        }

        public record Destination(String description) {

            public Destination {
                Objects.requireNonNull(description, "description cannot be null");
            }
        }
    }

    public record Centroid(String type, List<BigDecimal> coordinates) {

        public Centroid {
            Objects.requireNonNull(type, "type cannot be null");
            Objects.requireNonNull(coordinates, "coordinates cannot be null");
        }
    }
}
