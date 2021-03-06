package io.github.robertograham.departureapi.response;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder(builderMethodName = "newBuilder", builderClassName = "Builder")
@Value
public class Departure {

    @NonNull
    String line;

    @NonNull
    String lineName;

    @NonNull
    String operator;

    long epochSecond;

    @NonNull
    String operatorName;

    String destination;

    String direction;
}