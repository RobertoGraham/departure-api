package io.github.robertograham.departureapi.service

import feign.FeignException
import feign.Request
import feign.Response
import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.*
import io.github.robertograham.departureapi.exception.BusStopNotFoundException
import io.github.robertograham.departureapi.response.BusStop
import io.github.robertograham.departureapi.response.Departure
import reactor.test.StepVerifier
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
        transportApiClient.places(null,
                null,
                null,
                null,
                null,
                null,
                busStopId,
                { it == [Type.BUS_STOP] }) >>
                new PlacesResponse(ZonedDateTime.now(), 'source', 'acknowledgements', [new PlacesResponse.Member(Type.BUS_STOP, 'name', 'description', BigDecimal.ZERO, BigDecimal.ONE, 0, busStopId, 0)])

        expect:
        StepVerifier.create(subject.getBusStop(busStopId))
                .expectNext(new BusStop('busStopId', 'name', 'description', BigDecimal.ZERO, BigDecimal.ONE))
                .verifyComplete()
    }

    def "get nearby bus stops"() {
        given: "a valid longitude and latitude pair"
        def longitude = BigDecimal.ZERO
        def latitude = BigDecimal.ONE

        and: "a places response from the Transport API is stubbed"
        transportApiClient.places(latitude,
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

        expect: "the returned bus stops to only be mapped from places with a Type of BUS_STOP"
        StepVerifier.create(subject.getNearbyBusStops(longitude, latitude)
                .map { busStops ->
                    busStops.collect { it.id() }
                })
                .expectNext([Type.BUS_STOP.name()])
                .verifyComplete()
    }

    def "get bus stop departures"() {
        given:
        def busStopId = "busStopId"

        and:
        transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO) >>
                new BusStopDeparturesResponse('', '', ZonedDateTime.now(), '', '', Bearing.NORTH, '', '', ['': [new BusStopDeparturesResponse.Departure('mode', 'line', 'lineName', 'direction', 'operator', LocalDate.MIN, LocalDate.EPOCH, LocalTime.MAX, LocalTime.MAX, LocalTime.MIN, 'source', 'dir', null, 'operatorName')]], new BusStopDeparturesResponse.Location('', []))

        expect:
        StepVerifier.create(subject.getDepartures(busStopId))
                .expectNext([new Departure('line', 'lineName', 'operator', LocalDate.EPOCH.atTime(LocalTime.MIN)
                        .atZone(ZoneId.of("Europe/London"))
                        .toEpochSecond(), 'operatorName', 'direction', 'dir')])
                .verifyComplete()
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

        expect:
        StepVerifier.create(subject.getDepartures('busStopId'))
                .verifyError BusStopNotFoundException
    }

    def "get bus stop throws a BusStopNotFoundException"() {
        given:
        transportApiClient.places(*_) >> new PlacesResponse(ZonedDateTime.now(), 'source', 'acknowledgements', [])

        expect:
        StepVerifier.create(subject.getBusStop('busStopId'))
                .verifyError BusStopNotFoundException
    }
}
