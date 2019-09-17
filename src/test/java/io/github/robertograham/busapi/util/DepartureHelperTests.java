package io.github.robertograham.busapi.util;

import io.github.robertograham.busapi.client.dto.BusStopDeparturesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
final class DepartureHelperTests {

    @Autowired
    private DepartureHelper departureHelper;

    @Test
    void createDepartureList() {
        final var busStopDeparturesResponseDeparture = BusStopDeparturesResponse.Departure.newBuilder()
                .mode("mode")
                .line("line")
                .lineName("lineName")
                .direction("direction")
                .operator("operator")
                .date(LocalDate.MIN)
                .expectedDepartureDate(LocalDate.EPOCH)
                .aimedDepartureTime(LocalTime.MAX)
                .expectedDepartureTime(LocalTime.MAX)
                .bestDepartureEstimate(LocalTime.MIN)
                .source("source")
                .dir("dir")
                .operatorName("operatorName")
                .build();
        final var busStopDeparturesResponse = BusStopDeparturesResponse.newBuilder()
                .atcoCode("")
                .smsCode("")
                .requestTime(ZonedDateTime.now())
                .name("")
                .stopName("")
                .bearing("")
                .indicator("")
                .locality("")
                .departures(Map.of("", List.of(busStopDeparturesResponseDeparture)))
                .location(BusStopDeparturesResponse.Location.newBuilder()
                        .type("")
                        .coordinates(Collections.emptyList())
                        .build())
                .build();

        final var departureList = departureHelper.createDepartureList(busStopDeparturesResponse);

        assertThat(departureList).hasSize(1);
        final var departure = departureList.get(0);
        assertThat(departure.getDirection()).isEqualTo(busStopDeparturesResponseDeparture.getDir());
        assertThat(departure.getDestination()).isEqualTo(busStopDeparturesResponseDeparture.getDirection());
        assertThat(departure.getLine()).isEqualTo(busStopDeparturesResponseDeparture.getLine());
        assertThat(departure.getLineName()).isEqualTo(busStopDeparturesResponseDeparture.getLineName());
        assertThat(departure.getOperator()).isEqualTo(busStopDeparturesResponseDeparture.getOperator());
        assertThat(departure.getOperatorName()).isEqualTo(busStopDeparturesResponseDeparture.getOperatorName());
    }
}