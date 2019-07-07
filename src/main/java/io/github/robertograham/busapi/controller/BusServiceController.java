package io.github.robertograham.busapi.controller;

import io.github.robertograham.busapi.client.dto.BusServiceResponse;
import io.github.robertograham.busapi.service.BusServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/busServices")
final class BusServiceController {

    private final BusServiceService busServiceService;

    @Autowired
    BusServiceController(final BusServiceService busServiceService) {
        this.busServiceService = busServiceService;
    }

    @GetMapping("/{operatorCode}/{line}")
    private BusServiceResponse getBusService(@PathVariable final String operatorCode,
                                             @PathVariable final String line) {
        return busServiceService.getBusService(operatorCode, line);
    }
}
