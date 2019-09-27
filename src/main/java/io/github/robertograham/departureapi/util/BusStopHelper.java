package io.github.robertograham.departureapi.util;

import io.github.robertograham.departureapi.client.dto.PlacesResponse;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.dto.BusStop;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class BusStopHelper {

    private BusStopHelper() {
    }

    public static List<BusStop> createBusStopList(final PlacesResponse placesResponse) {
        return createBusStopList(placesResponse.getMembers());
    }

    private static List<BusStop> createBusStopList(final List<PlacesResponse.Member> members) {
        return members.stream()
            .filter((final var member) -> Type.BUS_STOP.getValue().equals(member.getType()))
            .map(BusStopHelper::createBusStop)
            .collect(Collectors.toList());
    }

    public static BusStop createBusStop(final PlacesResponse.Member member) {
        Objects.requireNonNull(member, "member cannot be null");
        if (!Type.BUS_STOP.getValue().equals(member.getType()))
            throw new IllegalArgumentException(String.format("Expected member with type \"%s\" got \"%s\" instead", Type.BUS_STOP.getValue(), member.getType()));
        return BusStop.newBuilder()
            .id(member.getAtcoCode())
            .name(member.getName())
            .locality(member.getDescription())
            .latitude(member.getLatitude())
            .longitude(member.getLongitude())
            .build();
    }
}
