package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.departureapi.response.Departure;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

final class DepartureHelper {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/London");

    private DepartureHelper() {
    }

    static Departure createDeparture(final BusStopDeparturesResponse.Departure departure) {
        return new Departure(departure.line(), resolveDepartureLineName(departure), departure.operator(), resolveDepartureEpochSecond(departure), resolveDepartureOperatorName(departure), departure.direction(), departure.dir());
    }

    private static long resolveDepartureEpochSecond(final BusStopDeparturesResponse.Departure departure) {
        return Optional.ofNullable(departure.expectedDepartureDate())
            .or(() -> Optional.ofNullable(departure.date()))
            .orElseGet(() -> LocalDate.now(ZONE_ID))
            .atTime(departure.bestDepartureEstimate())
            .atZone(ZONE_ID)
            .toEpochSecond();
    }

    private static String resolveDepartureLineName(final BusStopDeparturesResponse.Departure departure) {
        return Optional.ofNullable(departure.lineName())
            .orElseGet(departure::line);
    }

    private static String resolveDepartureOperatorName(final BusStopDeparturesResponse.Departure departure) {
        return Optional.ofNullable(departure.operatorName())
            .orElseGet(departure::operator);
    }
}
