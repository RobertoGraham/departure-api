package io.github.robertograham.departureapi.controller;

import feign.FeignException;
import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.response.Departure;
import io.github.robertograham.departureapi.service.BusStopService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/busStops")
final class BusStopController {

    private final BusStopService busStopService;

    BusStopController(final BusStopService busStopService) {
        this.busStopService = Objects.requireNonNull(busStopService, "busStopService cannot be null");
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BusStop> getNearbyBusStops(@RequestParam final BigDecimal latitude,
                                           @RequestParam final BigDecimal longitude) {
        return busStopService.getNearbyBusStops(longitude, latitude);
    }

    @GetMapping(value = "/{busStopId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public BusStop getBusStop(@PathVariable final String busStopId) {
        return busStopService.getBusStop(busStopId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No bus stop found for id: %s", busStopId)));
    }

    @GetMapping(value = "/{busStopId}/departures", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Departure> getBusStopDepartures(@PathVariable final String busStopId) {
        try {
            return busStopService.getDepartures(busStopId);
        } catch (final FeignException feignException) {
            if (HttpStatus.NOT_FOUND.value() == feignException.status())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No bus stop found for id: %s", busStopId), feignException);
            throw feignException;
        }
    }
}
