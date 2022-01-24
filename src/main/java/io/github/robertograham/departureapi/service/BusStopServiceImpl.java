package io.github.robertograham.departureapi.service;

import feign.FeignException.NotFound;
import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.Group;
import io.github.robertograham.departureapi.client.dto.NextBuses;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.exception.BusStopNotFoundException;
import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.response.Departure;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
final class BusStopServiceImpl implements BusStopService {

    private final TransportApiClient transportApiClient;

    public BusStopServiceImpl(final TransportApiClient transportApiClient) {
        this.transportApiClient = Objects.requireNonNull(transportApiClient, "transportApiClient cannot be null");
    }

    @Override
    public List<BusStop> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        return transportApiClient.places(latitude, longitude, null, null, null, null, null, Type.BUS_STOP)
            .members().stream()
            .filter(Objects::nonNull)
            .filter((final var member) -> Type.BUS_STOP == member.type())
            .map(BusStopHelper::createBusStop)
            .toList();
    }

    @Override
    public BusStop getBusStop(final String busStopId) {
        return transportApiClient.places(null, null, null, null, null, null, busStopId, Type.BUS_STOP)
            .members().stream()
            .filter(Objects::nonNull)
            .filter((final var member) -> Type.BUS_STOP == member.type())
            .filter((final var member) -> busStopId.equals(member.atcoCode()))
            .findFirst()
            .map(BusStopHelper::createBusStop)
            .orElseThrow(() -> new BusStopNotFoundException(busStopId));
    }

    @Override
    public List<Departure> getDepartures(final String busStopId) {
        try {
            return transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO)
                .departures()
                .values().stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(DepartureHelper::createDeparture)
                .toList();
        } catch (final NotFound exception) {
            throw new BusStopNotFoundException(busStopId, exception);
        }
    }
}
