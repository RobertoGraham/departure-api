package io.github.robertograham.busapi.controller;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.BusServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
final class TestController {

    private final TransportApiClient transportApiClient;

    @Autowired
    private TestController(final TransportApiClient transportApiClient) {
        this.transportApiClient = transportApiClient;
    }

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private BusServiceResponse test() {
        return transportApiClient.busService("SL", "59");
    }
}
