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

    public static List<BusStop> createBusStopList(final List<PlacesResponse.Member> members) {
        Objects.requireNonNull(members, "members cannot be null");
        if (members.stream()
            .anyMatch((final var member) -> member == null || Type.BUS_STOP != member.getType()))
            throw new IllegalArgumentException(String.format("members cannot contain a null value or a Member without a type of %s", Type.BUS_STOP.name()));
        return members.stream()
            .map(BusStopHelper::createBusStop)
            .collect(Collectors.toList());
    }

    public static BusStop createBusStop(final PlacesResponse.Member member) {
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
}
