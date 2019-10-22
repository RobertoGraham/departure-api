package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.dto.BusRouteResponse;
import io.github.robertograham.departureapi.client.dto.PlacesResponse;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.dto.BusStop;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class BusStopHelper {

    static BusStop createBusStop(final PlacesResponse.Member member) {
        Objects.requireNonNull(member, "member cannot be null");
        if (Type.BUS_STOP != member.getType())
            throw new IllegalArgumentException(String.format("member must have a type of %s", Type.BUS_STOP.name()));
        return BusStop.newBuilder()
            .id(member.getAtcoCode())
            .name(member.getName())
            .locality(member.getDescription())
            .latitude(member.getLatitude())
            .longitude(member.getLongitude())
            .build();
    }

    static BusStop createBusStop(final BusRouteResponse.Stop stop) {
        Objects.requireNonNull(stop, "stop cannot be null");
        return BusStop.newBuilder()
            .id(stop.getAtcoCode())
            .name(stop.getName())
            .locality(stop.getLocality())
            .latitude(stop.getLatitude())
            .longitude(stop.getLongitude())
            .build();
    }
}
