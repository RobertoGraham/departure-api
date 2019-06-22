package io.github.robertograham.busapi.controller;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.PlacesResponse;
import io.github.robertograham.busapi.client.dto.Type;
import io.github.robertograham.busapi.client.dto.TypeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/places")
final class PlacesController {

    private final TransportApiClient transportApiClient;

    @Autowired
    private PlacesController(final TransportApiClient transportApiClient) {
        this.transportApiClient = Objects.requireNonNull(transportApiClient);
    }

    @GetMapping(value = "/busStops", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private PlacesResponse busStops(@RequestParam final BigDecimal latitude,
                                    @RequestParam final BigDecimal longitude) {
        return transportApiClient.places(latitude, longitude, null, null, null, null, null, TypeSet.newBuilder()
            .type(Type.BUS_STOP)
            .build());
    }
}
