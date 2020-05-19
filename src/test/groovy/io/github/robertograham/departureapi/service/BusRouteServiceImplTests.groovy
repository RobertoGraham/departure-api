package io.github.robertograham.departureapi.service

import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.Bearing
import io.github.robertograham.departureapi.client.dto.BusRouteResponse
import io.github.robertograham.departureapi.client.dto.Stops
import io.github.robertograham.departureapi.response.BusStop
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

        when: "a request for the bus route is made"
        def epochSecondToBusStopListMap = subject.getBusRoute(operator, line, busStopId, direction, epochSecond)

        then: "a request to the Transport API is made and a bus route is received"
        1 * transportApiClient.busRoute(operator,
                line,
                direction,
                busStopId,
                LocalDate.ofInstant(Instant.ofEpochSecond(epochSecond), ZONE_ID),
                LocalTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZONE_ID),
                false,
                Stops.ONWARD) >>
                BusRouteResponse.newBuilder()
                        .requestTime(ZonedDateTime.now())
                        .operator("")
                        .operatorName("")
                        .line("")
                        .lineName("")
                        .originAtcoCode("")
                        .dir("")
                        .id("")
                        .stops(stops)
                        .build()

        and:
        epochSecondToBusStopListMap == instants.indexed().collectEntries { final index, final instant ->
            [(instant.epochSecond): [BusStop.newBuilder()
                                             .id(stops[index].atcoCode)
                                             .name(stops[index].name)
                                             .locality(stops[index].locality)
                                             .latitude(stops[index].latitude)
                                             .longitude(stops[index].longitude)
                                             .build()]]
        }
    }

    private static List<BusRouteResponse.Stop> createStops(final List<Instant> instants) {
        instants.indexed()
                .collect { final index, final instant ->
                    BusRouteResponse.Stop.newBuilder()
                            .time(LocalTime.ofInstant(instant, ZONE_ID))
                            .date(LocalDate.ofInstant(instant, ZONE_ID))
                            .atcoCode("busStopId$index")
                            .name("name$index")
                            .stopName("")
                            .smsCode("")
                            .locality("locality$index")
                            .bearing(Bearing.NORTH)
                            .indicator("")
                            .latitude(BigDecimal.valueOf(index))
                            .longitude(BigDecimal.valueOf(index))
                            .build()
                }
    }
}
