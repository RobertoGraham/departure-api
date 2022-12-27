package io.github.robertograham.departureapi.client;

import io.github.robertograham.departureapi.client.dto.BusRouteResponse;
import io.github.robertograham.departureapi.client.dto.BusServiceResponse;
import io.github.robertograham.departureapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.departureapi.client.dto.Group;
import io.github.robertograham.departureapi.client.dto.NextBuses;
import io.github.robertograham.departureapi.client.dto.PlacesResponse;
import io.github.robertograham.departureapi.client.dto.Stops;
import io.github.robertograham.departureapi.client.dto.TypeSetContainer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface TransportApiClient {

    @GetExchange(value = "/bus/services/{operator}:{line}.json", accept = APPLICATION_JSON_VALUE)
    Mono<BusServiceResponse> busService(@PathVariable String operator,
                                        @PathVariable String line);

    @GetExchange(value = "/bus/stop/{atcoCode}/live.json", accept = APPLICATION_JSON_VALUE)
    Mono<BusStopDeparturesResponse> busStopDepartures(@PathVariable String atcoCode,
                                                      @RequestParam(required = false) Group group,
                                                      @RequestParam(required = false) Integer limit,
                                                      @RequestParam(value = "nextbuses", required = false) NextBuses nextBuses);

    @GetExchange(value = "/bus/stop/{atcoCode}/{date}/{time}/timetable.json", accept = APPLICATION_JSON_VALUE)
    Mono<BusStopDeparturesResponse> busStopDepartures(@PathVariable String atcoCode,
                                                      @PathVariable(required = false) LocalDate date,
                                                      @PathVariable(required = false) LocalTime time,
                                                      @RequestParam(required = false) Group group,
                                                      @RequestParam(required = false) Integer limit);

    @GetExchange(value = "/bus/route/{operator}/{line}/{direction}/{atcoCode}/{date}/{time}/timetable.json", accept = APPLICATION_JSON_VALUE)
    Mono<BusRouteResponse> busRoute(@PathVariable String operator,
                                    @PathVariable String line,
                                    @PathVariable String direction,
                                    @PathVariable String atcoCode,
                                    @PathVariable(required = false) LocalDate date,
                                    @PathVariable(required = false) LocalTime time,
                                    @RequestParam(value = "edge_geometry", required = false) Boolean edgeGeometry,
                                    @RequestParam(required = false) Stops stops);

    @GetExchange(value = "/places.json", accept = APPLICATION_JSON_VALUE)
    Mono<PlacesResponse> places(@RequestParam(value = "lat", required = false) BigDecimal latitude,
                                @RequestParam(value = "lon", required = false) BigDecimal longitude,
                                @RequestParam(value = "max_lat", required = false) BigDecimal maxLatitude,
                                @RequestParam(value = "max_lon", required = false) BigDecimal maxLongitude,
                                @RequestParam(value = "min_lat", required = false) BigDecimal minLatitude,
                                @RequestParam(value = "min_lon", required = false) BigDecimal minLongitude,
                                @RequestParam(required = false) String query,
                                @RequestParam(value = "type", required = false) TypeSetContainer typeSetContainer);
}
