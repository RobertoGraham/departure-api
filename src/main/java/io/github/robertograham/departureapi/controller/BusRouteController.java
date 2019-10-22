package io.github.robertograham.departureapi.controller;

import io.github.robertograham.departureapi.dto.BusStop;
import io.github.robertograham.departureapi.service.BusRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/busRoutes")
@RequiredArgsConstructor
final class BusRouteController {

    private final BusRouteService busRouteService;

    @GetMapping(value = "/{operator}/{line}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<Long, List<BusStop>> busRouteResponse(@PathVariable final String operator,
                                                     @PathVariable final String line,
                                                     @RequestParam final String busStopId,
                                                     @RequestParam final String direction,
                                                     @RequestParam final long epochSecond) {
        return busRouteService.busRoute(operator, line, busStopId, direction, epochSecond);
    }
}
