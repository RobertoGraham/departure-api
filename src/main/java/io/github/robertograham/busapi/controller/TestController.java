package io.github.robertograham.busapi.controller;

import io.bitbucket.robertograham.transportapi.TransportApi;
import io.bitbucket.robertograham.transportapi.dto.request.busstopdepartures.NextBuses;
import io.bitbucket.robertograham.transportapi.dto.response.BusStopDeparturesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
final class TestController {

    private final TransportApi transportApi;

    @Autowired
    TestController(final TransportApi transportApi) {
        this.transportApi = transportApi;
    }

    @GetMapping("/test")
    BusStopDeparturesResponse test() {
        return transportApi.busStopDepartures("490000077E", null, null, NextBuses.NO);
    }
}
