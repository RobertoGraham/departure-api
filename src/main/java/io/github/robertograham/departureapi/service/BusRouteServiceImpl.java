package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.BusRouteResponse;
import io.github.robertograham.departureapi.client.dto.Stops;
import io.github.robertograham.departureapi.response.BusStop;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
final class BusRouteServiceImpl implements BusRouteService {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/London");

    private final TransportApiClient transportApiClient;

    BusRouteServiceImpl(final TransportApiClient transportApiClient) {
        this.transportApiClient = Objects.requireNonNull(transportApiClient, "transportApiClient cannot be null");
    }

    @Override
    public Map<Long, List<BusStop>> getBusRoute(final String operator, final String line, final String busStopId, final String direction, final long epochSecond) {
        final var zonedDateTime = Instant.ofEpochSecond(epochSecond)
            .atZone(ZONE_ID);
        return transportApiClient.busRoute(operator, line, direction, busStopId, zonedDateTime.toLocalDate(), zonedDateTime.toLocalTime(), false, Stops.ONWARD)
            .stops().stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(this::mapStopToEpochSecond, Collectors.mapping(BusStopHelper::createBusStop, Collectors.toList())));
    }

    private long mapStopToEpochSecond(final BusRouteResponse.Stop stop) {
        return stop.date()
            .atTime(stop.time())
            .atZone(ZONE_ID)
            .toEpochSecond();
    }
}
