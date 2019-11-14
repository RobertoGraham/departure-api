package io.github.robertograham.departureapi.service

import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.*
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

@Subject([BusStopServiceImpl, BusStopHelper, DepartureHelper])
final class BusStopServiceImplTests extends Specification {

    private def transportApiClient = Mock(TransportApiClient)
    private def busStopService = new BusStopServiceImpl(transportApiClient)

    def "a bus stop DTO with the correct values is created and returned when requesting it by its ID"() {
        given: "a mapped bus stop ID"
        def busStopId = "busStopId"

        and: "a"
        def placesResponseMember = PlacesResponse.Member.newBuilder()
                .accuracy(0)
                .atcoCode(busStopId)
                .description("description")
                .distance(1)
                .latitude(BigDecimal.ZERO)
                .longitude(BigDecimal.ONE)
                .name("name")
                .type(Type.BUS_STOP)
                .build()

        when:
        def busStop = busStopService.getBusStop(busStopId)
                .orElseThrow()

        then:
        1 * transportApiClient.places(null,
                null,
                null,
                null,
                null,
                null,
                busStopId,
                {
                    it.types == [Type.BUS_STOP] as Set
                }) >>
                PlacesResponse.newBuilder()
                        .members([placesResponseMember])
                        .requestTime(ZonedDateTime.now())
                        .source("source")
                        .acknowledgements("acknowledgements")
                        .build()

        and:
        verifyAll(busStop) {
            id == busStopId
            latitude == placesResponseMember.latitude
            longitude == placesResponseMember.longitude
            locality == placesResponseMember.description
            name == placesResponseMember.name
        }
    }

    def "get nearby bus stops"() {
        given: "a valid longitude and latitude pair"
        def longitude = BigDecimal.ZERO
        def latitude = BigDecimal.ONE

        when: "a request for nearby bus stops is made"
        def busStops = busStopService.getNearbyBusStops(longitude, latitude)

        then: "a request to the Transport API is made and places are received"
        1 * transportApiClient.places(latitude,
                longitude,
                null,
                null,
                null,
                null,
                null,
                {
                    it.types == [Type.BUS_STOP] as Set
                }) >>
                PlacesResponse.newBuilder()
                        .acknowledgements("acknowledgments")
                        .requestTime(ZonedDateTime.now())
                        .source("source")
                        .members([*Type.values()
                                .collect {
                                    PlacesResponse.Member.newBuilder()
                                            .accuracy(0)
                                            .atcoCode(it.value)
                                            .description("description")
                                            .distance(1)
                                            .latitude(BigDecimal.ZERO)
                                            .longitude(BigDecimal.ONE)
                                            .name("name")
                                            .type(it)
                                            .build()
                                },
                                  null])
                        .build()

        and: "only the place with a Type of BUS_STOP was used to create a DTO"
        busStops.collect { it.id } == [Type.BUS_STOP.value]
    }

    def "get bus stop departures"() {
        given:
        def busStopId = "busStopId"

        and:
        def busStopDeparturesResponseDeparture = BusStopDeparturesResponse.Departure.newBuilder()
                .mode("mode")
                .line("line")
                .lineName("lineName")
                .direction("direction")
                .operator("operator")
                .date(LocalDate.MIN)
                .expectedDepartureDate(LocalDate.EPOCH)
                .aimedDepartureTime(LocalTime.MAX)
                .expectedDepartureTime(LocalTime.MAX)
                .bestDepartureEstimate(LocalTime.MIN)
                .source("source")
                .dir("dir")
                .operatorName("operatorName")
                .build()

        when:
        def departures = busStopService.getDepartures(busStopId)

        then:
        1 * transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO) >>
                BusStopDeparturesResponse.newBuilder()
                        .atcoCode("")
                        .smsCode("")
                        .requestTime(ZonedDateTime.now())
                        .name("")
                        .stopName("")
                        .bearing(Bearing.NORTH)
                        .indicator("")
                        .locality("")
                        .departures(["": [busStopDeparturesResponseDeparture]])
                        .location(BusStopDeparturesResponse.Location.newBuilder()
                                .type("")
                                .coordinates([])
                                .build())
                        .build()

        and:
        departures.size() == 1

        and:
        verifyAll(departures[0]) {
            direction == busStopDeparturesResponseDeparture.dir
            destination == busStopDeparturesResponseDeparture.direction
            line == busStopDeparturesResponseDeparture.line
            lineName == busStopDeparturesResponseDeparture.lineName.orElse(null)
            operator == busStopDeparturesResponseDeparture.operator
            operatorName == busStopDeparturesResponseDeparture.operatorName.orElse(null)
            epochSecond == busStopDeparturesResponseDeparture.expectedDepartureDate.map {
                it.atTime(busStopDeparturesResponseDeparture.bestDepartureEstimate)
                        .atZone(ZoneId.of("Europe/London"))
                        .toEpochSecond()
            }
                    .orElse(null)
        }
    }
}
