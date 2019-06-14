package io.github.robertograham.busapi.client;

import io.github.robertograham.busapi.client.dto.BusStopDeparturesResponse;
import io.github.robertograham.busapi.client.dto.Group;
import io.github.robertograham.busapi.client.dto.NextBuses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

@FeignClient(
    name = "transportApiClient",
    url = "${transportApiClient.url}",
    configuration = TransportApiClientConfiguration.class
)
public interface TransportApiClient {

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
}
