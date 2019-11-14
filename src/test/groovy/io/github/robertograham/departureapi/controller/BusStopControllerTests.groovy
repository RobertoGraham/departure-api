package io.github.robertograham.departureapi.controller

import com.fasterxml.jackson.databind.ObjectMapper
import feign.FeignException
import feign.Request
import feign.Response
import io.github.robertograham.departureapi.DepartureApiApplication
import io.github.robertograham.departureapi.client.TransportApiClient
import io.github.robertograham.departureapi.client.dto.PlacesResponse
import io.github.robertograham.departureapi.client.dto.Type
import io.github.robertograham.departureapi.client.dto.TypeSet
import io.github.robertograham.departureapi.response.BusStop
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification

import java.time.ZonedDateTime

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest(classes = [DepartureApiApplication])
final class BusStopControllerTests extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private ObjectMapper objectMapper

    @SpringBean
    private TransportApiClient transportApiClient = Stub()

    def "get nearby bus stops success"() {
        given: "a latitude and longitude pair"
        def latitude = BigDecimal.ZERO
        def longitude = BigDecimal.ONE

        and: "a stubbed places response member"
        def placesResponseMember = PlacesResponse.Member.newBuilder()
                .accuracy(0)
                .atcoCode("atcoCode")
                .description("description")
                .distance(1)
                .latitude(BigDecimal.ZERO)
                .longitude(BigDecimal.ONE)
                .name("name")
                .type(Type.BUS_STOP)
                .build()

        and: "a stubbed Transport API client places result"
        transportApiClient.places(latitude, longitude, null, null, null, null, null, TypeSet.newBuilder()
                .type(Type.BUS_STOP)
                .build()) >>
                PlacesResponse.newBuilder()
                        .members([placesResponseMember])
                        .requestTime(ZonedDateTime.now())
                        .source("source")
                        .acknowledgements("acknowledgements")
                        .build()

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/busStops")
                .param("longitude", longitude as String)
                .param("latitude", latitude as String))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString([
                        BusStop.newBuilder()
                                .id(placesResponseMember.atcoCode)
                                .latitude(placesResponseMember.latitude)
                                .longitude(placesResponseMember.longitude)
                                .locality(placesResponseMember.description)
                                .name(placesResponseMember.name)
                                .build()
                ])))
    }

    def "get nearby bus stops bad gateway"() {
        given: "a latitude and longitude pair"
        def latitude = BigDecimal.ZERO
        def longitude = BigDecimal.ONE

        and: "Transport API client places call throws feign exception"
        transportApiClient.places(latitude, longitude, null, null, null, null, null, TypeSet.newBuilder()
                .type(Type.BUS_STOP)
                .build()) >> {
            throw FeignException.errorStatus("", Response.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .request(Request.create(Request.HttpMethod.GET, "", [:], null))
                    .headers([:])
                    .build())
        }

        expect: "a bad gateway response"
        mockMvc.perform(MockMvcRequestBuilders.get("/busStops")
                .param("longitude", longitude as String)
                .param("latitude", latitude as String))
                .andExpect(status().isBadGateway())
    }

    def "get bus stop by ID success"() {
        given:
        def busStopId = "busStopId"

        and:
        def placesResponseMember = PlacesResponse.Member.newBuilder()
                .accuracy(0)
                .atcoCode(busStopId)
                .description("description")
                .distance(1)
                .latitude(BigDecimal.ZERO)
                .longitude(BigDecimal.ONE)
                .name("name")
                .type(Type.BUS_STOP)
                .build()

        and:
        transportApiClient.places(null, null, null, null, null, null, busStopId, TypeSet.newBuilder()
                .type(Type.BUS_STOP)
                .build()) >>
                PlacesResponse.newBuilder()
                        .members([placesResponseMember])
                        .requestTime(ZonedDateTime.now())
                        .source("source")
                        .acknowledgements("acknowledgements")
                        .build()

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/busStops/${busStopId}"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(BusStop.newBuilder()
                        .id(placesResponseMember.atcoCode)
                        .latitude(placesResponseMember.latitude)
                        .longitude(placesResponseMember.longitude)
                        .locality(placesResponseMember.description)
                        .name(placesResponseMember.name)
                        .build())))
    }

    def "get bus stop by ID not found"() {
        given:
        def busStopId = "busStopId"

        and:
        transportApiClient.places(null, null, null, null, null, null, busStopId, TypeSet.newBuilder()
                .type(Type.BUS_STOP)
                .build()) >>
                PlacesResponse.newBuilder()
                        .members([])
                        .requestTime(ZonedDateTime.now())
                        .source("source")
                        .acknowledgements("acknowledgements")
                        .build()

        expect:
        mockMvc.perform(MockMvcRequestBuilders.get("/busStops/${busStopId}"))
                .andExpect(status().isNotFound())
    }
}