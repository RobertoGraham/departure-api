package io.github.robertograham.departureapi.util;

import io.github.robertograham.departureapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.departureapi.dto.Departure;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class DepartureHelper {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/London");

    public List<Departure> createDepartureList(final BusStopDeparturesResponse busStopDeparturesResponse) {
        return createDepartureList(busStopDeparturesResponse.getDepartures());
    }

    private List<Departure> createDepartureList(final Map<String, List<BusStopDeparturesResponse.Departure>> lineToDepartureListMap) {
        return lineToDepartureListMap.values().stream()
            .flatMap(List::stream)
            .map(this::createDeparture)
            .collect(Collectors.toList());
    }

    private Departure createDeparture(final BusStopDeparturesResponse.Departure departure) {
        return Departure.newBuilder()
            .epochSecond(resolveDepartureTimeEpochSecond(departure))
            .direction(departure.getDir())
            .destination(departure.getDirection())
            .line(departure.getLine())
            .lineName(resolveDepartureLineName(departure))
            .operator(departure.getOperator())
            .operatorName(resolveDepartureOperatorName(departure))
            .build();
    }

    private long resolveDepartureTimeEpochSecond(final BusStopDeparturesResponse.Departure departure) {
        final var departureLocalDate = Optional.ofNullable(departure.getExpectedDepartureDate())
            .or(() -> Optional.ofNullable(departure.getDate()))
            .orElseGet(() -> LocalDate.now(ZONE_ID));
        return departureLocalDate.atTime(departure.getBestDepartureEstimate())
            .atZone(ZONE_ID)
            .toEpochSecond();
    }

    private String resolveDepartureLineName(final BusStopDeparturesResponse.Departure departure) {
        return Optional.ofNullable(departure.getLineName())
            .orElseGet(departure::getLine);
    }

    private String resolveDepartureOperatorName(BusStopDeparturesResponse.Departure departure) {
        return Optional.ofNullable(departure.getOperatorName())
            .orElseGet(departure::getOperator);
    }
}
