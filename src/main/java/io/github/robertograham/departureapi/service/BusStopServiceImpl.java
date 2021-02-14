package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.Group;
import io.github.robertograham.departureapi.client.dto.NextBuses;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.response.Departure;
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

    @NonNull
    private final QuadTreeService quadTreeService;

    @Override
    public List<BusStop> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        return quadTreeService.getNearbyBusStops(longitude, latitude);
    }

    @Override
    public Optional<BusStop> getBusStop(final String busStopId) {
        return transportApiClient.places(null, null, null, null, null, null, busStopId, Type.BUS_STOP)
            .getMembers().stream()
            .filter(Objects::nonNull)
            .filter((final var member) -> Type.BUS_STOP == member.getType())
            .filter((final var member) -> busStopId.equals(member.getAtcoCode()))
            .findFirst()
            .map(BusStopHelper::createBusStop);
    }

    @Override
    public List<Departure> getDepartures(final String busStopId) {
        return transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO)
            .getDepartures()
            .values().stream()
            .filter(Objects::nonNull)
            .flatMap(List::stream)
            .filter(Objects::nonNull)
            .map(DepartureHelper::createDeparture)
            .collect(Collectors.toList());
    }
}
