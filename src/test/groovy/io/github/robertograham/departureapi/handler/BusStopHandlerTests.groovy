package io.github.robertograham.departureapi.handler

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.robertograham.departureapi.configuration.BusStopRouterConfiguration
import io.github.robertograham.departureapi.exception.BusStopNotFoundException
import io.github.robertograham.departureapi.exception.ProviderErrorException
import io.github.robertograham.departureapi.response.BusStop
import io.github.robertograham.departureapi.service.BusStopService
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
import static org.springframework.http.HttpStatus.BAD_GATEWAY
import static org.springframework.http.MediaType.APPLICATION_JSON

@WebFluxTest
@ContextConfiguration(classes = [BusStopRouterConfiguration, BusStopHandler, BusStopServiceStubConfiguration])
final class BusStopHandlerTests extends Specification {

    @Autowired
    private WebTestClient webTestClient

    @Autowired
    private ObjectMapper objectMapper

    @Autowired
    private BusStopService busStopService

    def "get nearby bus stops success"() {
        given: "coordinates"
        final def latitude = ZERO
        final def longitude = ONE

        and: "bus stops"
        final def busStops = [new BusStop('id', 'name', 'locality', ZERO, ONE)]

        and: "a stubbed getNearbyBusStops result"
        busStopService.getNearbyBusStops(longitude, latitude) >> Mono.just(busStops)

        expect: "the response to have the correct content"
        webTestClient.get()
                .uri { uriBuilder ->
                    uriBuilder.path('/busStops')
                            .queryParam('longitude', "{longitude}")
                            .queryParam('latitude', "{latitude}")
                            .build(longitude, latitude)
                }
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(busStops))
    }

    def "get nearby bus stops bad gateway"() {
        given: "coordinates"
        final def latitude = ZERO
        final def longitude = ONE

        and: "getNearbyBusStops throws a feign exception"
        busStopService.getNearbyBusStops(longitude, latitude) >> Mono.error(new ProviderErrorException(null))

        expect: "a bad gateway response"
        webTestClient.get()
                .uri { uriBuilder ->
                    uriBuilder.path('/busStops')
                            .queryParam('longitude', "{longitude}")
                            .queryParam('latitude', "{latitude}")
                            .build(longitude, latitude)
                }
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(BAD_GATEWAY)
                .expectBody().jsonPath('$.message').isEqualTo('Communication error occurred with data provider')
    }

    def "get bus stop by ID success"() {
        given: "a bus stop ID"
        final def busStopId = "busStopId"

        and: "a bus stop"
        final def busStop = new BusStop('id', 'name', 'locality', ZERO, ONE)

        and: "a stubbed getBusStop result"
        busStopService.getBusStop(busStopId) >> Mono.just(busStop)

        expect:
        webTestClient.get()
                .uri { uriBuilder ->
                    uriBuilder.path('/busStops/{busStopId}')
                            .build(busStopId)
                }
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().json(objectMapper.writeValueAsString(busStop))
    }

    def "get bus stop by ID not found"() {
        given: "a bus stop ID"
        def busStopId = "busStopId"

        and: "a stubbed getBusStop result"
        busStopService.getBusStop(busStopId) >> Mono.error(new BusStopNotFoundException(busStopId))

        expect: "a not found response"
        webTestClient.get()
                .uri { uriBuilder ->
                    uriBuilder.path('/busStops/{busStopId}')
                            .build(busStopId)
                }
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath('$.message').isEqualTo("No bus stop found for id: $busStopId" as String)
    }

    @TestConfiguration
    static class BusStopServiceStubConfiguration {

        private final def detachedMockFactory = new DetachedMockFactory()

        @Bean
        BusStopService busStopService() {
            detachedMockFactory.Stub(BusStopService)
        }
    }
}
