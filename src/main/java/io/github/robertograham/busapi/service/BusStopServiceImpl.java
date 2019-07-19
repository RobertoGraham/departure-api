package io.github.robertograham.busapi.service;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.Group;
import io.github.robertograham.busapi.client.dto.NextBuses;
import io.github.robertograham.busapi.client.dto.Type;
import io.github.robertograham.busapi.client.dto.TypeSet;
import io.github.robertograham.busapi.dto.BusStop;
import io.github.robertograham.busapi.dto.Departure;
import io.github.robertograham.busapi.factory.BusStopFactory;
import io.github.robertograham.busapi.factory.DepartureFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
final class BusStopServiceImpl implements BusStopService {

    private final TransportApiClient transportApiClient;

    private final BusStopFactory busStopFactory;

    private final DepartureFactory departureFactory;

    @Autowired
    BusStopServiceImpl(final TransportApiClient transportApiClient, final BusStopFactory busStopFactory, final DepartureFactory departureFactory) {
        this.transportApiClient = transportApiClient;
        this.busStopFactory = busStopFactory;
        this.departureFactory = departureFactory;
    }

    @Override
    public List<BusStop> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        final var placesResponse = transportApiClient.places(latitude, longitude, null, null, null, null, null, TypeSet.newBuilder()
                .type(Type.BUS_STOP)
                .build());
        return busStopFactory.createBusStopList(placesResponse);
    }

    @Override
    public Optional<BusStop> getBusStop(final String busStopId) {
        final var placesResponse = transportApiClient.places(null, null, null, null, null, null, busStopId, TypeSet.newBuilder()
                .type(Type.BUS_STOP)
                .build());
        return placesResponse.getMember().stream()
                .filter((final var member) -> busStopId.equals(member.getAtcoCode()))
                .findFirst()
                .map(busStopFactory::createBusStop);
    }

    @Override
    public List<Departure> getDepartures(final String busStopId) {
        final var busStopDeparturesResponse = transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO);
        return departureFactory.createDepartureList(busStopDeparturesResponse);
    }
}
