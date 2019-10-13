package io.github.robertograham.departureapi.service

import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.PlacesResponse
import io.github.robertograham.departureapi.client.dto.Type
import spock.lang.Specification

import java.time.ZonedDateTime

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
        def placesResponse = new PlacesResponse.Builder(members: [placesResponseMember],
                requestTime: ZonedDateTime.now(),
                source: "source",
                acknowledgements: "acknowledgements")
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
                { it.types == [Type.BUS_STOP] as Set }) >> placesResponse
        busStop.id == busStopId
        busStop.latitude == placesResponseMember.latitude
        busStop.longitude == placesResponseMember.longitude
        busStop.locality == placesResponseMember.description
        busStop.name == placesResponseMember.name
    }

    def "get nearby bus stops"() {
        given:
        def longitude = BigDecimal.ZERO
        def latitude = BigDecimal.ONE
        def placesResponse = new PlacesResponse.Builder(acknowledgements: "acknowledgments",
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
                { it.types == [Type.BUS_STOP] as Set }) >> placesResponse
        busStops.collect { it.id } == [Type.BUS_STOP.value]
    }
}
