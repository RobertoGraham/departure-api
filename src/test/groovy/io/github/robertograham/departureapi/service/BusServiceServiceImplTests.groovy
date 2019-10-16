package io.github.robertograham.departureapi.service

import io.github.robertograham.departureapi.client.TransportApiClient
import spock.lang.Specification
import spock.lang.Subject

@Subject(BusServiceServiceImpl)
final class BusServiceServiceImplTests extends Specification {

    private def transportApiClient = Mock(TransportApiClient)
    private def busServiceService = new BusServiceServiceImpl(transportApiClient)

    def "get bus service"() {
        given:
        def operatorCode = "operatorCode"
        def line = "line"

        when:
        busServiceService.getBusService(operatorCode, line)

        then:
        1 * transportApiClient.busService(operatorCode, line)
    }
}
