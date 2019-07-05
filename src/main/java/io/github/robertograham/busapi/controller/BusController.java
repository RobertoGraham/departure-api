package io.github.robertograham.busapi.controller;

import io.github.robertograham.busapi.dto.BusStop;
import io.github.robertograham.busapi.dto.BusStopDepartures;
import io.github.robertograham.busapi.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/busStops")
final class BusController {

    private final BusService busService;

    @Autowired
    public BusController(final BusService busService) {
        this.busService = busService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private List<BusStop> getNearbyBusStops(@RequestParam final BigDecimal latitude,
                                            @RequestParam final BigDecimal longitude) {
        return busService.getNearbyBusStops(longitude, latitude);
    }

    @GetMapping(value = "/{busStopId}/departures", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private BusStopDepartures getBusStopDeparutes(@PathVariable final String busStopId) {
        return busService.getBusStopDepartures(busStopId);
    }
}
