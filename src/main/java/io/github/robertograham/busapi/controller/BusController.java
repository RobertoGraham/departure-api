package io.github.robertograham.busapi.controller;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.busapi.client.dto.Group;
import io.github.robertograham.busapi.client.dto.NextBuses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bus")
final class BusController {

    private final TransportApiClient transportApiClient;

    @Autowired
    public BusController(TransportApiClient transportApiClient) {
        this.transportApiClient = transportApiClient;
    }

    @GetMapping(value = "/departures/{atcoCode}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private BusStopDeparturesResponse departures(@PathVariable final String atcoCode) {
        return transportApiClient.busStopDepartures(atcoCode, Group.ROUTE, null, NextBuses.NO);
    }
}
