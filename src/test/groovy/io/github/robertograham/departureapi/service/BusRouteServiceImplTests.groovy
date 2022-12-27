package io.github.robertograham.departureapi.service

import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.Bearing
import io.github.robertograham.departureapi.client.dto.BusRouteResponse
import io.github.robertograham.departureapi.client.dto.Stops
import io.github.robertograham.departureapi.response.BusStop
import reactor.test.StepVerifier
import spock.lang.Specification
import spock.lang.Subject

import java.time.*

@Subject([BusRouteServiceImpl, BusStopHelper])
final class BusRouteServiceImplTests extends Specification {

    private static final def ZONE_ID = ZoneId.of "Europe/London"

    private def transportApiClient = Mock TransportApiClient
    private def subject = new BusRouteServiceImpl(transportApiClient)

    def "a bus route with the correct structure is created and returned"() {
        given: "request parameters"
        def operator = "operator"
        def line = "line"
        def busStopId = "busStopId"
        def direction = "direction"
        def epochSecond = 0L

        and: "departure times"
        def instants = (0..1).collect { Instant.ofEpochSecond it * 60L }
        def stops = createStops(instants)

        and: "a bus route response from the Transport API is stubbed"
        transportApiClient.busRoute(operator,
                line,
                direction,
                busStopId,
                LocalDate.ofInstant(Instant.ofEpochSecond(epochSecond), ZONE_ID),
                LocalTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZONE_ID),
                false,
                Stops.ONWARD) >>
                new BusRouteResponse(ZonedDateTime.now(), '', '', '', '', '', '', '', stops)

        expect: "the returned bus route to be correct"
        StepVerifier.create(subject.getBusRoute(operator, line, busStopId, direction, epochSecond))
                .expectNext(instants.indexed().collectEntries { final index, final instant ->
                    [(instant.epochSecond): [stops[index].with {
                        new BusStop(atcoCode(), name(), locality(), latitude(), longitude())
                    }]]
                })
                .verifyComplete()
    }

    private static List<BusRouteResponse.Stop> createStops(final List<Instant> instants) {
        instants.indexed()
                .collect { final index, final instant ->
                    new BusRouteResponse.Stop(LocalTime.ofInstant(instant, ZONE_ID), LocalDate.ofInstant(instant, ZONE_ID), "busStopId$index", "name$index", '', '', "locality$index", Bearing.NORTH, '', BigDecimal.valueOf(index), BigDecimal.valueOf(index), null)
                }
    }
}
