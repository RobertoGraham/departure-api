package io.github.robertograham.busapi.service;

import io.github.robertograham.busapi.dto.BusStop;
import io.github.robertograham.busapi.dto.BusStopDepartures;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BusStopService {

    List<BusStop> getNearbyBusStops(BigDecimal longitude, BigDecimal latitude);

    Optional<BusStop> getBusStop(String busStopId);

    BusStopDepartures getBusStopDepartures(String busStopId);
}
