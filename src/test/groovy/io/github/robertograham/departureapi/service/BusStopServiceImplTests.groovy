package io.github.robertograham.departureapi.service

import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.*
import io.github.robertograham.departureapi.dto.BusStop
import io.github.robertograham.departureapi.dto.Departure
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

    def "get bus stop by ID"() {
        given:
        def busStopId = "busStopId"
        def placesResponseMember = new PlacesResponse.Member.Builder(accuracy: 0,
                atcoCode: busStopId,
                description: "description",
                distance: 1,
                latitude: BigDecimal.ZERO,
                longitude: BigDecimal.ONE,
                name: "name",
                type: Type.BUS_STOP)
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
                }) >> new PlacesResponse.Builder(members: [placesResponseMember],
                requestTime: ZonedDateTime.now(),
                source: "source",
                acknowledgements: "acknowledgements")
                .build()
        busStop == new BusStop.Builder(id: busStopId,
                latitude: placesResponseMember.latitude,
                longitude: placesResponseMember.longitude,
                locality: placesResponseMember.description,
                name: placesResponseMember.name)
                .build()
    }

    def "get nearby bus stops"() {
        given:
        def longitude = BigDecimal.ZERO
        def latitude = BigDecimal.ONE

        when:
        def busStops = busStopService.getNearbyBusStops(longitude, latitude)

        then:
        1 * transportApiClient.places(latitude,
                longitude,
                null,
                null,
                null,
                null,
                null,
                {
                    it.types == [Type.BUS_STOP] as Set
                }) >> new PlacesResponse.Builder(acknowledgements: "acknowledgments",
                requestTime: ZonedDateTime.now(),
                source: "source",
                members: [*Type.values()
                        .collect {
                            new PlacesResponse.Member.Builder(accuracy: 0,
                                    atcoCode: it.value,
                                    description: "description",
                                    distance: 1,
                                    latitude: BigDecimal.ZERO,
                                    longitude: BigDecimal.ONE,
                                    name: "name",
                                    type: it)
                                    .build()
                        },
                          null])
                .build()
        busStops.collect { it.id } == [Type.BUS_STOP.value]
    }

    def "get bus stop departures"() {
        given:
        def busStopId = "busStopId"
        def busStopDeparturesResponseDeparture = new BusStopDeparturesResponse.Departure.Builder(mode: "mode",
                line: "line",
                lineName: "lineName",
                direction: "direction",
                operator: "operator",
                date: LocalDate.MIN,
                expectedDepartureDate: LocalDate.EPOCH,
                aimedDepartureTime: LocalTime.MAX,
                expectedDepartureTime: LocalTime.MAX,
                bestDepartureEstimate: LocalTime.MIN,
                source: "source",
                dir: "dir",
                operatorName: "operatorName")
                .build()

        when:
        def departures = busStopService.getDepartures(busStopId)

        then:
        1 * transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO) >> new BusStopDeparturesResponse.Builder(atcoCode: "",
                smsCode: "",
                requestTime: ZonedDateTime.now(),
                name: "",
                stopName: "",
                bearing: Bearing.NORTH,
                indicator: "",
                locality: "",
                departures: ["": [busStopDeparturesResponseDeparture]],
                location: new BusStopDeparturesResponse.Location.Builder(type: "",
                        coordinates: [])
                        .build())
                .build()
        departures == [new Departure.Builder(direction: busStopDeparturesResponseDeparture.dir,
                destination: busStopDeparturesResponseDeparture.direction,
                line: busStopDeparturesResponseDeparture.line,
                lineName: busStopDeparturesResponseDeparture.lineName,
                operator: busStopDeparturesResponseDeparture.operator,
                operatorName: busStopDeparturesResponseDeparture.operatorName,
                epochSecond: busStopDeparturesResponseDeparture.expectedDepartureDate.atTime(busStopDeparturesResponseDeparture.bestDepartureEstimate)
                        .atZone(ZoneId.of("Europe/London"))
                        .toEpochSecond())
                               .build()]
    }
}
