package io.github.robertograham.busapi.service;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.*;
import io.github.robertograham.busapi.dto.BusStop;
import io.github.robertograham.busapi.dto.Departure;
import io.github.robertograham.busapi.util.BusStopHelper;
import io.github.robertograham.busapi.util.DepartureHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
final class BusStopServiceImpl implements BusStopService {

    @NonNull
    private final TransportApiClient transportApiClient;

    @NonNull
    private final BusStopHelper busStopHelper;

    @NonNull
    private final DepartureHelper departureHelper;

    @Override
    public List<BusStop> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        final var placesResponse = transportApiClient.places(latitude, longitude, null, null, null, null, null, TypeSet.builder()
                .type(Type.BUS_STOP)
                .build());
        return busStopHelper.createBusStopList(placesResponse);
    }

    @Override
    public Optional<BusStop> getBusStop(final String busStopId) {
        final var placesResponse = transportApiClient.places(null, null, null, null, null, null, busStopId, TypeSet.builder()
                .type(Type.BUS_STOP)
                .build());
        return placesResponse.getMembers().stream()
                .filter((final PlacesResponse.Member member) -> Type.BUS_STOP.getValue().equals(member.getType()))
                .filter((final var member) -> busStopId.equals(member.getAtcoCode()))
                .findFirst()
                .map(busStopHelper::createBusStop);
    }

    @Override
    public List<Departure> getDepartures(final String busStopId) {
        final var busStopDeparturesResponse = transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO);
        return departureHelper.createDepartureList(busStopDeparturesResponse);
    }
}
