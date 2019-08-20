package io.github.robertograham.busapi.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Builder(builderClassName = "Builder", builderMethodName = "newBuilder")
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