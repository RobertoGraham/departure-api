package io.github.robertograham.busapi.dto;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@lombok.Builder(builderClassName = "Builder", builderMethodName = "newBuilder")
@Value
public class Departure {

    @NonNull
    String line;

    @NonNull
    String lineName;

    @NonNull
    String operator;

    @NonNull
    LocalDate date;

    @NonNull
    LocalTime time;

    @NonNull
    String operatorName;

    String destination;

    String direction;
}