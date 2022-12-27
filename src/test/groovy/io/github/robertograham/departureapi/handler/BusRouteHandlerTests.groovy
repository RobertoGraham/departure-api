package io.github.robertograham.departureapi.handler

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.robertograham.departureapi.configuration.BusRouteRouterConfiguration
import io.github.robertograham.departureapi.response.BusStop
import io.github.robertograham.departureapi.service.BusRouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Mono
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.ZERO
import static org.springframework.http.MediaType.APPLICATION_JSON

@WebFluxTest
@ContextConfiguration(classes = [BusRouteRouterConfiguration, BusRouteHandler, BusRouteServiceStubConfiguration])
final class BusRouteHandlerTests extends Specification {

    @Autowired
    private WebTestClient webTestClient

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private BusRouteService busRouteService

    def "get bus routes success"() {
        given: "request parameters"
        final def operator = "operator"
        final def line = "line"
        final def busStopId = "busStopId"
        final def direction = "direction"
        final def epochSecond = 0L

        and: "a bus route"
        final def busRoute = [0L: [new BusStop('id', 'name', 'locality', ZERO, ONE)]]

        and: "a stubbed getBusRoute result"
        busRouteService.getBusRoute(operator, line, busStopId, direction, epochSecond) >> Mono.just(busRoute)

        expect: "the response to have the correct content"
        webTestClient.get()
                .uri { uriBuilder ->
                    uriBuilder.path("/busRoutes/{operator}/{line}")
                            .queryParam('busStopId', "{busStopId}")
                            .queryParam('direction', "{direction}")
                            .queryParam('epochSecond', "{epochSecond}")
                            .build(operator, line, busStopId, direction, epochSecond)
                }
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(busRoute))
    }

    @TestConfiguration
    static class BusRouteServiceStubConfiguration {

        private final def detachedMockFactory = new DetachedMockFactory()

        @Bean
        BusRouteService busRouteService() {
            detachedMockFactory.Stub(BusRouteService)
        }
    }
}
