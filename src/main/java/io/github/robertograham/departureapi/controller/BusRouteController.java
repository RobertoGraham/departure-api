package io.github.robertograham.departureapi.controller;

import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.service.BusRouteService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/busRoutes")
final class BusRouteController {

    private final BusRouteService busRouteService;

    BusRouteController(final BusRouteService busRouteService) {
        this.busRouteService = Objects.requireNonNull(busRouteService, "busRouteService cannot be null");
    }

    @GetMapping(value = "/{operator}/{line}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<Long, List<BusStop>> getBusRoute(@PathVariable final String operator,
                                                @PathVariable final String line,
                                                @RequestParam final String busStopId,
                                                @RequestParam final String direction,
                                                @RequestParam final long epochSecond) {
        return busRouteService.getBusRoute(operator, line, busStopId, direction, epochSecond);
    }
}
