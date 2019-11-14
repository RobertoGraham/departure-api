package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.departureapi.response.Departure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class DepartureHelper {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/London");

    static Departure createDeparture(final BusStopDeparturesResponse.Departure departure) {
        return Departure.newBuilder()
            .epochSecond(resolveDepartureEpochSecond(departure))
            .direction(departure.getDir())
            .destination(departure.getDirection())
            .line(departure.getLine())
            .lineName(resolveDepartureLineName(departure))
            .operator(departure.getOperator())
            .operatorName(resolveDepartureOperatorName(departure))
            .build();
    }

    private static long resolveDepartureEpochSecond(final BusStopDeparturesResponse.Departure departure) {
        return departure.getExpectedDepartureDate()
            .or(departure::getDate)
            .orElseGet(() -> LocalDate.now(ZONE_ID))
            .atTime(departure.getBestDepartureEstimate())
            .atZone(ZONE_ID)
            .toEpochSecond();
    }

    private static String resolveDepartureLineName(final BusStopDeparturesResponse.Departure departure) {
        return departure.getLineName()
            .orElseGet(departure::getLine);
    }

    private static String resolveDepartureOperatorName(final BusStopDeparturesResponse.Departure departure) {
        return departure.getOperatorName()
            .orElseGet(departure::getOperator);
    }
}
