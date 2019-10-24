package io.github.robertograham.departureapi.service

import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.Bearing
import io.github.robertograham.departureapi.client.dto.BusRouteResponse
import io.github.robertograham.departureapi.client.dto.Stops
import io.github.robertograham.departureapi.dto.BusStop
import spock.lang.Specification
import spock.lang.Subject

import java.time.*

@Subject([BusRouteServiceImpl, BusStopHelper])
final class BusRouteServiceImplTests extends Specification {

    private def transportApiClient = Mock(TransportApiClient)
    private def busRouteService = new BusRouteServiceImpl(transportApiClient)
    private static final def ZONE_ID = ZoneId.of("Europe/London")

    def ""() {
        given: "request parameters"
        def operator = "operator"
        def line = "line"
        def busStopId = "busStopId"
        def direction = "direction"
        def epochSecond = 0L

        and: "departure times"
        def instant1 = Instant.ofEpochSecond(0L)
        def stop1 = BusRouteResponse.Stop.newBuilder()
                .time(LocalTime.ofInstant(instant1, ZONE_ID))
                .date(LocalDate.ofInstant(instant1, ZONE_ID))
                .atcoCode("busStopId1")
                .name("name1")
                .stopName("")
                .smsCode("")
                .locality("locality1")
                .bearing(Bearing.NORTH)
                .indicator("")
                .latitude(BigDecimal.ZERO)
                .longitude(BigDecimal.ONE)
                .build()
        def instant2 = Instant.ofEpochSecond(60L)
        def stop2 = BusRouteResponse.Stop.newBuilder()
                .time(LocalTime.ofInstant(instant2, ZONE_ID))
                .date(LocalDate.ofInstant(instant2, ZONE_ID))
                .atcoCode("busStopId2")
                .name("name2")
                .stopName("")
                .smsCode("")
                .locality("locality2")
                .bearing(Bearing.NORTH_EAST)
                .indicator("")
                .latitude(BigDecimal.TEN)
                .longitude(BigDecimal.TEN.add(BigDecimal.ONE))
                .build()

        when: "a request for the bus route is made"
        def epochSecondToBusStopListMap = busRouteService.busRoute(operator, line, busStopId, direction, epochSecond)

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
                        .stops([stop1, stop2])
                        .build()

        and:
        epochSecondToBusStopListMap == [(instant1.epochSecond): [BusStop.newBuilder()
                                                                         .id(stop1.atcoCode)
                                                                         .name(stop1.name)
                                                                         .locality(stop1.locality)
                                                                         .latitude(stop1.latitude)
                                                                         .longitude(stop1.longitude)
                                                                         .build()],
                                        (instant2.epochSecond): [BusStop.newBuilder()
                                                                         .id(stop2.atcoCode)
                                                                         .name(stop2.name)
                                                                         .locality(stop2.locality)
                                                                         .latitude(stop2.latitude)
                                                                         .longitude(stop2.longitude)
                                                                         .build()]]
    }
}
