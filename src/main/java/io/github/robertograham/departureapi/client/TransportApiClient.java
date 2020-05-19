package io.github.robertograham.departureapi.client;

import io.github.robertograham.departureapi.client.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@FeignClient(
    name = "transportApiClient",
    url = "${transportApiClient.url}",
    configuration = TransportApiClientConfiguration.class,
    primary = false
)
public interface TransportApiClient {

    @GetMapping(value = "/bus/services/{operator}:{line}.json", produces = MediaType.APPLICATION_JSON_VALUE)
    BusServiceResponse busService(@PathVariable String operator,
                                  @PathVariable String line);

    @GetMapping(value = "/bus/stop/{atcoCode}/live.json", produces = MediaType.APPLICATION_JSON_VALUE)
    BusStopDeparturesResponse busStopDepartures(@PathVariable String atcoCode,
                                                @RequestParam(required = false) Group group,
                                                @RequestParam(required = false) Integer limit,
                                                @RequestParam(value = "nextbuses", required = false) NextBuses nextBuses);

    @GetMapping(value = "/bus/stop/{atcoCode}/{date}/{time}/timetable.json", produces = MediaType.APPLICATION_JSON_VALUE)
    BusStopDeparturesResponse busStopDepartures(@PathVariable String atcoCode,
                                                @PathVariable(required = false) LocalDate date,
                                                @PathVariable(required = false) LocalTime time,
                                                @RequestParam(required = false) Group group,
                                                @RequestParam(required = false) Integer limit);

    @GetMapping(value = "/bus/route/{operator}/{line}/{direction}/{atcoCode}/{date}/{time}/timetable.json", produces = MediaType.APPLICATION_JSON_VALUE)
    BusRouteResponse busRoute(@PathVariable String operator,
                              @PathVariable String line,
                              @PathVariable String direction,
                              @PathVariable String atcoCode,
                              @PathVariable(required = false) LocalDate date,
                              @PathVariable(required = false) LocalTime time,
                              @RequestParam(value = "edge_geometry", required = false) Boolean edgeGeometry,
                              @RequestParam(required = false) Stops stops);

    @GetMapping(value = "/places.json", produces = MediaType.APPLICATION_JSON_VALUE)
    PlacesResponse places(@RequestParam(value = "lat", required = false) BigDecimal latitude,
                          @RequestParam(value = "lon", required = false) BigDecimal longitude,
                          @RequestParam(value = "max_lat", required = false) BigDecimal maxLatitude,
                          @RequestParam(value = "max_lon", required = false) BigDecimal maxLongitude,
                          @RequestParam(value = "min_lat", required = false) BigDecimal minLatitude,
                          @RequestParam(value = "min_lon", required = false) BigDecimal minLongitude,
                          @RequestParam(required = false) String query,
                          @RequestParam(value = "type", required = false) Type... types);
}
