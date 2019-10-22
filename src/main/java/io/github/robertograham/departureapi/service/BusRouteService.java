package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.dto.BusStop;

import java.util.List;
import java.util.Map;

public interface BusRouteService {

    Map<Long, List<BusStop>> busRoute(String operator, String line, String busStopId, String direction, long epochSecond);
}
