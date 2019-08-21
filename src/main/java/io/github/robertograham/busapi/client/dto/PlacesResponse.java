package io.github.robertograham.busapi.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Builder
@Value
@JsonDeserialize(builder = PlacesResponse.PlacesResponseBuilder.class)
@JsonPOJOBuilder(withPrefix = "")
public class PlacesResponse {

    @JsonProperty("request_time")
    @NonNull
    ZonedDateTime requestTime;

    @JsonProperty("source")
    @NonNull
    String source;

    @JsonProperty("acknowledgements")
    @NonNull
    String acknowledgements;

    @JsonProperty("member")
    @NonNull
    List<Member> members;

    @Builder
    @Value
    @JsonDeserialize(builder = Member.MemberBuilder.class)
    @JsonPOJOBuilder(withPrefix = "")
    public static class Member {

        @JsonProperty("type")
        @NonNull
        String type;

        @JsonProperty("name")
        @NonNull
        String name;

        @JsonProperty("description")
        String description;

        @JsonProperty("latitude")
        @NonNull
        BigDecimal latitude;

        @JsonProperty("longitude")
        @NonNull
        BigDecimal longitude;

        @JsonProperty("accuracy")
        @NonNull
        Integer accuracy;

        @JsonProperty("atcocode")
        String atcoCode;

        @JsonProperty("distance")
        Integer distance;
    }
}
