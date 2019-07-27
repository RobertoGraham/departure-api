package io.github.robertograham.busapi.factory;

import io.github.robertograham.busapi.client.dto.PlacesResponse;
import io.github.robertograham.busapi.client.dto.Type;
import io.github.robertograham.busapi.dto.BusStop;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public final class BusStopFactory {

    public List<BusStop> createBusStopList(final PlacesResponse placesResponse) {
        return createBusStopList(placesResponse.getMembers());
    }

    private List<BusStop> createBusStopList(final List<PlacesResponse.Member> members) {
        return members.stream()
                .filter((final PlacesResponse.Member member) -> Type.BUS_STOP.getValue().equals(member.getType()))
                .map(this::createBusStop)
                .collect(Collectors.toList());
    }

    public BusStop createBusStop(final PlacesResponse.Member member) {
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
