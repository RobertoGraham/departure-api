package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.BusRouteResponse;
import io.github.robertograham.departureapi.client.dto.Stops;
import io.github.robertograham.departureapi.dto.BusStop;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
final class BusRouteServiceImpl implements BusRouteService {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/London");

    private final TransportApiClient transportApiClient;

    @Override
    public Map<Long, List<BusStop>> busRoute(final String operator, final String line, final String busStopId, final String direction, final long epochSecond) {
        final var instant = Instant.ofEpochSecond(epochSecond);
        return transportApiClient.busRoute(operator, line, direction, busStopId, LocalDate.ofInstant(instant, ZONE_ID), LocalTime.ofInstant(instant, ZONE_ID), false, Stops.ONWARD)
            .getStops().stream()
            .collect(Collectors.groupingBy(this::mapStopToEpochSecond, Collectors.mapping(BusStopHelper::createBusStop, Collectors.toList())));
    }

    private long mapStopToEpochSecond(final BusRouteResponse.Stop stop) {
        return stop.getDate()
            .atTime(stop.getTime())
            .atZone(ZONE_ID)
            .toEpochSecond();
    }
}
