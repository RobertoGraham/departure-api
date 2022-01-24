package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.response.Departure;

import java.math.BigDecimal;
import java.util.List;

public interface BusStopService {

    List<BusStop> getNearbyBusStops(BigDecimal longitude, BigDecimal latitude);

    BusStop getBusStop(String busStopId);

    List<Departure> getDepartures(String busStopId);
}
