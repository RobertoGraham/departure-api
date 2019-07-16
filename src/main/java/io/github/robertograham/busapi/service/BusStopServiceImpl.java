package io.github.robertograham.busapi.service;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.Group;
import io.github.robertograham.busapi.client.dto.NextBuses;
import io.github.robertograham.busapi.client.dto.Type;
import io.github.robertograham.busapi.client.dto.TypeSet;
import io.github.robertograham.busapi.dto.BusStop;
import io.github.robertograham.busapi.dto.BusStopDepartures;
import io.github.robertograham.busapi.factory.BusStopDeparturesFactory;
import io.github.robertograham.busapi.factory.BusStopFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
final class BusStopServiceImpl implements BusStopService {

    private final TransportApiClient transportApiClient;

    private final BusStopFactory busStopFactory;

    private final BusStopDeparturesFactory busStopDeparturesFactory;

    @Autowired
    BusStopServiceImpl(final TransportApiClient transportApiClient, final BusStopFactory busStopFactory, final BusStopDeparturesFactory busStopDeparturesFactory) {
        this.transportApiClient = transportApiClient;
        this.busStopFactory = busStopFactory;
        this.busStopDeparturesFactory = busStopDeparturesFactory;
    }

    @Override
    public List<BusStop> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        final var placesResponse = transportApiClient.places(latitude, longitude, null, null, null, null, null, TypeSet.newBuilder()
                .type(Type.BUS_STOP)
                .build());
        return busStopFactory.createBusStopList(placesResponse);
    }

    @Override
    public BusStopDepartures getBusStopDepartures(final String busStopId) {
        final var busStopDeparturesResponse = transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO);
        return busStopDeparturesFactory.createBusStopDepartures(busStopDeparturesResponse);
    }
}
