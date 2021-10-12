package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.dto.BusRouteResponse;
import io.github.robertograham.departureapi.client.dto.PlacesResponse;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.response.BusStop;

import java.util.Objects;

final class BusStopHelper {

    private BusStopHelper() {
    }

    static BusStop createBusStop(final PlacesResponse.Member member) {
        Objects.requireNonNull(member, "member cannot be null");
        if (Type.BUS_STOP != member.type())
            throw new IllegalArgumentException(String.format("member must have a type of %s", Type.BUS_STOP.name()));
        return new BusStop(member.atcoCode(), member.name(), member.description(), member.latitude(), member.longitude());
    }

    static BusStop createBusStop(final BusRouteResponse.Stop stop) {
        Objects.requireNonNull(stop, "stop cannot be null");
        return new BusStop(stop.atcoCode(), stop.name(), stop.locality(), stop.latitude(), stop.longitude());
    }
}
