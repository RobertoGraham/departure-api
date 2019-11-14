package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.response.Departure;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BusStopService {

    List<BusStop> getNearbyBusStops(BigDecimal longitude, BigDecimal latitude);

    Optional<BusStop> getBusStop(String busStopId);

    List<Departure> getDepartures(String busStopId);
}
