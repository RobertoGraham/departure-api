package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.Group;
import io.github.robertograham.departureapi.client.dto.NextBuses;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.client.dto.TypeSet;
import io.github.robertograham.departureapi.dto.BusStop;
import io.github.robertograham.departureapi.dto.Departure;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
final class BusStopServiceImpl implements BusStopService {

    @NonNull
    private final TransportApiClient transportApiClient;

    @Override
    public List<BusStop> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        return transportApiClient.places(latitude, longitude, null, null, null, null, null, TypeSet.newBuilder()
            .type(Type.BUS_STOP)
            .build())
            .getMembers().stream()
            .filter(Objects::nonNull)
            .filter((final var member) -> Type.BUS_STOP == member.getType())
            .map(BusStopHelper::createBusStop)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<BusStop> getBusStop(final String busStopId) {
        return transportApiClient.places(null, null, null, null, null, null, busStopId, TypeSet.newBuilder()
            .type(Type.BUS_STOP)
            .build())
            .getMembers().stream()
            .filter(Objects::nonNull)
            .filter((final var member) -> Type.BUS_STOP == member.getType())
            .filter((final var member) -> busStopId.equals(member.getAtcoCode()))
            .findFirst()
            .map(BusStopHelper::createBusStop);
    }

    @Override
    public List<Departure> getDepartures(final String busStopId) {
        final var busStopDeparturesResponse = transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO);
        return DepartureHelper.createDepartureList(busStopDeparturesResponse);
    }
}
