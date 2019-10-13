package io.github.robertograham.departureapi.controller;

import io.github.robertograham.departureapi.client.dto.BusServiceResponse;
import io.github.robertograham.departureapi.service.BusServiceService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/busServices")
@RequiredArgsConstructor
final class BusServiceController {

    @NonNull
    private final BusServiceService busServiceService;

    @GetMapping("/{operatorCode}/{line}")
    public BusServiceResponse getBusService(@PathVariable final String operatorCode,
                                            @PathVariable final String line) {
        return busServiceService.getBusService(operatorCode, line);
    }
}