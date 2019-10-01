package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.Group;
import io.github.robertograham.departureapi.client.dto.NextBuses;
import io.github.robertograham.departureapi.dto.BusStop;
import io.github.robertograham.departureapi.dto.Departure;
import io.github.robertograham.departureapi.util.BusStopHelper;
import io.github.robertograham.departureapi.util.DepartureHelper;
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

    @Override
    public List<BusStop> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        return BusStopHelper.createBusStopList(transportApiClient.nearbyBusStopPlacesMembers(latitude, longitude));
    }

    @Override
    public Optional<BusStop> getBusStop(final String busStopId) {
        return transportApiClient.busStopPlacesMemberById(busStopId)
            .map(BusStopHelper::createBusStop);
    }

    @Override
    public List<Departure> getDepartures(final String busStopId) {
        final var busStopDeparturesResponse = transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO);
        return DepartureHelper.createDepartureList(busStopDeparturesResponse);
    }
}
