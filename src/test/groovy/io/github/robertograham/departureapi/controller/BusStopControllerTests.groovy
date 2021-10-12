package io.github.robertograham.departureapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import feign.FeignException
import feign.Request
import feign.Response
import io.github.robertograham.departureapi.response.BusStop
import io.github.robertograham.departureapi.service.BusStopService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static feign.Request.HttpMethod.GET
import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.ZERO
import static java.nio.charset.StandardCharsets.UTF_8
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BusStopController)
final class BusStopControllerTests extends Specification {

    @Autowired
    private MockMvc mockMvc

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
        busStopService.getNearbyBusStops(longitude, latitude) >> busStops

        expect: "the response to have the correct content"
        mockMvc.perform(get("/busStops")
                .param("longitude", longitude as String)
                .param("latitude", latitude as String))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(busStops)))
    }

    def "get nearby bus stops bad gateway"() {
        given: "coordinates"
        final def latitude = ZERO
        final def longitude = ONE

        and: "getNearbyBusStops throws a feign exception"
        busStopService.getNearbyBusStops(longitude, latitude) >> {
            throw FeignException.errorStatus("", Response.builder()
                    .status(INTERNAL_SERVER_ERROR.value())
                    .request(Request.create(GET, "", [:], Request.Body.create("", UTF_8), null))
                    .headers([:])
                    .build())
        }

        expect: "a bad gateway response"
        mockMvc.perform(get("/busStops")
                .param("longitude", longitude as String)
                .param("latitude", latitude as String))
                .andExpect(status().isBadGateway())
    }

    def "get bus stop by ID success"() {
        given: "a bus stop ID"
        final def busStopId = "busStopId"

        and: "a bus stop"
        final def busStop = new BusStop('id', 'name', 'locality', ZERO, ONE)

        and: "a stubbed getBusStop result"
        busStopService.getBusStop(busStopId) >> Optional.of(busStop)

        expect:
        mockMvc.perform(get("/busStops/$busStopId"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(busStop)))
    }

    def "get bus stop by ID not found"() {
        given: "a bus stop ID"
        def busStopId = "busStopId"

        and: "a stubbed getBusStop result"
        busStopService.getBusStop(busStopId) >> Optional.empty()

        expect: "a not found response"
        mockMvc.perform(get("/busStops/$busStopId"))
                .andExpect(status().isNotFound())
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
