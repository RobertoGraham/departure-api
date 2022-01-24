package io.github.robertograham.departureapi.service

import feign.FeignException
import feign.Request
import feign.Response
import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.*
import io.github.robertograham.departureapi.exception.BusStopNotFoundException
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

import static feign.Request.HttpMethod.GET
import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.http.HttpStatus.NOT_FOUND

@Subject([BusStopServiceImpl, BusStopHelper, DepartureHelper])
final class BusStopServiceImplTests extends Specification {

    private def transportApiClient = Mock TransportApiClient
    private def subject = new BusStopServiceImpl(transportApiClient)

    def "a bus stop DTO with the correct values is created and returned when requesting it by its ID"() {
        given: "a mapped bus stop ID"
        def busStopId = "busStopId"

        and:
        def placesResponseMember = new PlacesResponse.Member(Type.BUS_STOP, 'name', 'description', BigDecimal.ZERO, BigDecimal.ONE, 0, busStopId, 0)

        when:
        def busStop = subject.getBusStop(busStopId)

        then:
        1 * transportApiClient.places(null,
                null,
                null,
                null,
                null,
                null,
                busStopId,
                { it == [Type.BUS_STOP] }) >>
                new PlacesResponse(ZonedDateTime.now(), 'source', 'acknowledgements', [placesResponseMember])

        and:
        verifyAll busStop, {
            id() == busStopId
            latitude() == placesResponseMember.latitude()
            longitude() == placesResponseMember.longitude()
            locality() == placesResponseMember.description()
            name() == placesResponseMember.name()
        }
    }

    def "get nearby bus stops"() {
        given: "a valid longitude and latitude pair"
        def longitude = BigDecimal.ZERO
        def latitude = BigDecimal.ONE

        when: "a request for nearby bus stops is made"
        def busStops = subject.getNearbyBusStops(longitude, latitude)

        then: "a request to the Transport API is made and places are received"
        1 * transportApiClient.places(latitude,
                longitude,
                null,
                null,
                null,
                null,
                null,
                { it == [Type.BUS_STOP] }) >>
                new PlacesResponse(ZonedDateTime.now(), 'source', 'acknowledgments', [*Type.values()
                        .collect {
                            new PlacesResponse.Member(it, 'name', 'description', BigDecimal.ZERO, BigDecimal.ONE, 0, it.name(), 1)
                        }, null])

        and: "only the place with a Type of BUS_STOP was used to create a DTO"
        busStops.collect { it.id() } == [Type.BUS_STOP.name()]
    }

    def "get bus stop departures"() {
        given:
        def busStopId = "busStopId"

        and:
        def busStopDeparturesResponseDeparture = new BusStopDeparturesResponse.Departure('mode', 'line', 'lineName', 'direction', 'operator', LocalDate.MIN, LocalDate.EPOCH, LocalTime.MAX, LocalTime.MAX, LocalTime.MIN, 'source', 'dir', null, 'operatorName')

        when:
        def departures = subject.getDepartures(busStopId)

        then:
        1 * transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO) >>
                new BusStopDeparturesResponse('', '', ZonedDateTime.now(), '', '', Bearing.NORTH, '', '', ['': [busStopDeparturesResponseDeparture]], new BusStopDeparturesResponse.Location('', []))

        and:
        departures.size() == 1

        and:
        verifyAll departures.first(), {
            direction() == busStopDeparturesResponseDeparture.dir()
            destination() == busStopDeparturesResponseDeparture.direction()
            line() == busStopDeparturesResponseDeparture.line()
            lineName() == busStopDeparturesResponseDeparture.lineName()
            operator() == busStopDeparturesResponseDeparture.operator()
            operatorName() == busStopDeparturesResponseDeparture.operatorName()
            epochSecond() == busStopDeparturesResponseDeparture.expectedDepartureDate()
                    ?.atTime(busStopDeparturesResponseDeparture.bestDepartureEstimate())
                    ?.atZone(ZoneId.of("Europe/London"))
                    ?.toEpochSecond()
        }
    }

    def "get departures handles NotFound exception"() {
        given:
        transportApiClient.busStopDepartures(*_) >> {
            throw FeignException.errorStatus("", Response.builder()
                    .status(NOT_FOUND.value())
                    .request(Request.create(GET, "", [:], Request.Body.create("", UTF_8), null))
                    .headers([:])
                    .build())
        }

        when:
        subject.getDepartures('busStopId')

        then:
        thrown(BusStopNotFoundException)
    }

    def "get bus stop throws a BusStopNotFoundException"() {
        given:
        transportApiClient.places(*_) >> new PlacesResponse(ZonedDateTime.now(), 'source', 'acknowledgements', [])

        when:
        subject.getBusStop('busStopId')

        then:
        thrown(BusStopNotFoundException)
    }
}
