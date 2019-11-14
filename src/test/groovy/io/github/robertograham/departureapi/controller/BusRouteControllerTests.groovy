package io.github.robertograham.departureapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.robertograham.departureapi.DepartureApiApplication
import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.Bearing
import io.github.robertograham.departureapi.client.dto.BusRouteResponse
import io.github.robertograham.departureapi.client.dto.Stops
import io.github.robertograham.departureapi.response.BusStop
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import java.time.*

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(classes = [DepartureApiApplication])
final class BusRouteControllerTests extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @SpringBean
    private TransportApiClient transportApiClient = Stub()

    private static final def ZONE_ID = ZoneId.of("Europe/London")

    def "get bus routes success"() {
        given:
        def operator = "operator"
        def line = "line"
        def busStopId = "busStopId"
        def direction = "direction"
        def epochSecond = 0L

        and:
        def instant = Instant.ofEpochSecond(0L)
        def stop = BusRouteResponse.Stop.newBuilder()
                .time(LocalTime.ofInstant(instant, ZONE_ID))
                .date(LocalDate.ofInstant(instant, ZONE_ID))
                .atcoCode("busStopId")
                .name("name")
                .stopName("")
                .smsCode("")
                .locality("locality")
                .bearing(Bearing.NORTH)
                .indicator("")
                .latitude(BigDecimal.ZERO)
                .longitude(BigDecimal.ONE)
                .build()

        and:
        transportApiClient.busRoute(operator,
                line,
                direction,
                busStopId,
                LocalDate.ofInstant(Instant.ofEpochSecond(epochSecond), ZONE_ID),
                LocalTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZONE_ID),
                false,
                Stops.ONWARD) >>
                BusRouteResponse.newBuilder()
                        .requestTime(ZonedDateTime.now())
                        .operator("")
                        .operatorName("")
                        .line("")
                        .lineName("")
                        .originAtcoCode("")
                        .dir("")
                        .id("")
                        .stops([stop])
                        .build()

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/busRoutes/$operator/$line")
                .param("busStopId", busStopId)
                .param("direction", direction)
                .param("epochSecond", epochSecond as String))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString([(instant.epochSecond): [BusStop.newBuilder()
                                                                                                           .id(stop.atcoCode)
                                                                                                           .name(stop.name)
                                                                                                           .locality(stop.locality)
                                                                                                           .latitude(stop.latitude)
                                                                                                           .longitude(stop.longitude)
                                                                                                           .build()]])))
    }
}
