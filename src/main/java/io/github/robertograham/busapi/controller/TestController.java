package io.github.robertograham.busapi.controller;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.busapi.client.dto.Group;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@Value
class TestController {

    TransportApiClient transportApiClient;

    @Autowired
    private TestController(final TransportApiClient transportApiClient) {
        this.transportApiClient = transportApiClient;
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private BusStopDeparturesResponse test() {
        return transportApiClient.busStopDepartures("490000077E", LocalDate.now(), LocalTime.now().plusHours(5L), Group.NO, 1);
    }
}
