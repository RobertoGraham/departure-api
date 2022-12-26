package io.github.robertograham.departureapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.robertograham.departureapi.response.BusStop
import io.github.robertograham.departureapi.service.BusRouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import static java.math.BigDecimal.ONE
import static java.math.BigDecimal.ZERO
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BusRouteController)
@ContextConfiguration
final class BusRouteControllerTests extends Specification {

    @Autowired
    private MockMvc mockMvc

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
        busRouteService.getBusRoute(operator, line, busStopId, direction, epochSecond) >> busRoute

        expect: "the response to have the correct content"
        mockMvc.perform(get("/busRoutes/$operator/$line")
                .param("busStopId", busStopId)
                .param("direction", direction)
                .param("epochSecond", epochSecond as String))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(busRoute)))
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
