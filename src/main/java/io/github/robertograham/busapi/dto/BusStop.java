package io.github.robertograham.busapi.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.math.BigDecimal;

@Builder(builderClassName = "Builder", builderMethodName = "newBuilder")
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
