package io.github.robertograham.busapi.factory;

import io.github.robertograham.busapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.busapi.dto.Departure;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public final class DepartureFactory {

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
                .date(Optional.ofNullable(departure.getExpectedDepartureDate()).or(() -> Optional.ofNullable(departure.getDate())).orElseGet(() -> LocalDate.now(ZoneId.of("Europe/London"))))
                .time(departure.getBestDepartureEstimate())
                .direction(departure.getDir())
                .destination(departure.getDirection())
                .line(departure.getLine())
                .lineName(Optional.ofNullable(departure.getLineName()).orElseGet(departure::getLine))
                .operator(departure.getOperator())
                .operatorName(Optional.ofNullable(departure.getOperatorName()).orElseGet(departure::getOperator))
                .build();
    }
}
