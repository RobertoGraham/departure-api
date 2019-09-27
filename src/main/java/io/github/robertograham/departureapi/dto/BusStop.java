package io.github.robertograham.departureapi.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Builder(builderMethodName = "newBuilder", builderClassName = "Builder")
@Value
public class BusStop {

    @NonNull
    String id;

    @NonNull
    String name;

    String locality;

    BigDecimal latitude;

    BigDecimal longitude;
}
