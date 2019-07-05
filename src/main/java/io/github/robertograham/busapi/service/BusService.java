package io.github.robertograham.busapi.service;

import io.github.robertograham.busapi.dto.BusStop;
import io.github.robertograham.busapi.dto.BusStopDepartures;

import java.math.BigDecimal;
import java.util.List;

public interface BusService {

    List<BusStop> getNearbyBusStops(BigDecimal longitude, BigDecimal latitude);

    BusStopDepartures getBusStopDepartures(String busStopId);
}
