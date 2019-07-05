package io.github.robertograham.busapi.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Builder(builderClassName = "Builder", builderMethodName = "newBuilder")
@Value
public class BusStopDepartures {

    @NonNull
    BusStop busStop;

    @NonNull
    List<Departure> departures;
}
