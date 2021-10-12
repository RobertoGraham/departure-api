package io.github.robertograham.departureapi.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public record PlacesResponse(@JsonProperty("request_time") ZonedDateTime requestTime, String source,
                             String acknowledgements, @JsonProperty("member") List<Member> members) {

    public PlacesResponse {
        Objects.requireNonNull(requestTime, "requestTime cannot be null");
        Objects.requireNonNull(source, "source cannot be null");
        Objects.requireNonNull(acknowledgements, "acknowledgements cannot be null");
        Objects.requireNonNull(members, "members cannot be null");
    }

    public record Member(Type type, String name, String description, BigDecimal latitude, BigDecimal longitude,
                         Integer accuracy, @JsonProperty("atcocode") String atcoCode, Integer distance) {

        public Member {
            Objects.requireNonNull(type, "type cannot be null");
            Objects.requireNonNull(name, "name cannot be null");
            Objects.requireNonNull(description, "description cannot be null");
            Objects.requireNonNull(latitude, "latitude cannot be null");
            Objects.requireNonNull(longitude, "longitude cannot be null");
            Objects.requireNonNull(accuracy, "accuracy cannot be null");
        }
    }
}
