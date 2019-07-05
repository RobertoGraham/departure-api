package io.github.robertograham.busapi.factory;

import io.github.robertograham.busapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.busapi.dto.BusStopDepartures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class BusStopDeparturesFactory {

    private final BusStopFactory busStopFactory;

    private final DepartureFactory departureFactory;

    @Autowired
    public BusStopDeparturesFactory(final BusStopFactory busStopFactory, final DepartureFactory departureFactory) {
        this.busStopFactory = busStopFactory;
        this.departureFactory = departureFactory;
    }

    public BusStopDepartures createBusStopDepartures(final BusStopDeparturesResponse busStopDeparturesResponse) {
        return BusStopDepartures.newBuilder()
                .busStop(busStopFactory.createBusStop(busStopDeparturesResponse))
                .departures(departureFactory.createDepartureList(busStopDeparturesResponse))
                .build();
    }
}
