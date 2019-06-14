package io.github.robertograham.busapi.controller;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.busapi.client.dto.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
final class TestController {

    private final TransportApiClient transportApiClient;

    @Autowired
    private TestController(final TransportApiClient transportApiClient) {
        this.transportApiClient = transportApiClient;
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private BusStopDeparturesResponse test() {
        BusStopDeparturesResponse busStopDeparturesResponse = transportApiClient.busStopDepartures("490000077E", LocalDate.now(), LocalTime.now().plusHours(5L), Group.NO, 1);
        return busStopDeparturesResponse;
    }
}
