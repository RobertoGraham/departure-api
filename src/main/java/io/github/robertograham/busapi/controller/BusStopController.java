package io.github.robertograham.busapi.controller;

import feign.FeignException;
import io.github.robertograham.busapi.dto.BusStop;
import io.github.robertograham.busapi.dto.BusStopDepartures;
import io.github.robertograham.busapi.service.BusStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/busStops")
final class BusStopController {

    private final BusStopService busStopService;

    @Autowired
    BusStopController(final BusStopService busStopService) {
        this.busStopService = busStopService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private List<BusStop> getNearbyBusStops(@RequestParam final BigDecimal latitude,
                                            @RequestParam final BigDecimal longitude) {
        try {
            return busStopService.getNearbyBusStops(longitude, latitude);
        } catch (final FeignException feignException) {
            throw createBadGatewayResponseStatusException();
        }
    }

    @GetMapping(value = "/{busStopId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private BusStop getBusStop(@PathVariable final String busStopId) {
        try {
            return busStopService.getBusStop(busStopId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No bus stop found for id: %s", busStopId)));
        } catch (final FeignException feignException) {
            throw createBadGatewayResponseStatusException();
        }
    }

    @GetMapping(value = "/{busStopId}/departures", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    private BusStopDepartures getBusStopDepartures(@PathVariable final String busStopId) {
        try {
            return busStopService.getBusStopDepartures(busStopId);
        } catch (final FeignException feignException) {
            if (HttpStatus.NOT_FOUND.value() == feignException.status())
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("No bus stop found for id: %s", busStopId), feignException);
            else
                throw createBadGatewayResponseStatusException();
        }
    }

    private ResponseStatusException createBadGatewayResponseStatusException() {
        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Communication error occurred with data provider");
    }
}
