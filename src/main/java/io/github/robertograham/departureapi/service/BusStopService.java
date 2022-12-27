package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.response.Departure;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface BusStopService {

    Mono<List<BusStop>> getNearbyBusStops(BigDecimal longitude, BigDecimal latitude);

    Mono<BusStop> getBusStop(String busStopId);

    Mono<List<Departure>> getDepartures(String busStopId);
}
