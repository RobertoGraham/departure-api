package io.github.robertograham.departureapi.util;

import io.github.robertograham.departureapi.client.dto.PlacesResponse;
import io.github.robertograham.departureapi.client.dto.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest
final class BusStopHelperTests {

    @Autowired
    private BusStopHelper busStopHelper;

    @Test
    @DisplayName("createBusStopList filters out member objects that don't have the bus stop type")
    void createBusStopList() {
        final var members = Arrays.stream(Type.values())
                .map(Type::getValue)
                .map((final var typeValueString) -> PlacesResponse.Member.newBuilder()
                        .type(typeValueString)
                        .name("")
                        .latitude(BigDecimal.ZERO)
                        .longitude(BigDecimal.ZERO)
                        .accuracy(0)
                        .atcoCode(typeValueString)
                        .build())
                .collect(Collectors.toList());

        final var busStops = busStopHelper.createBusStopList(PlacesResponse.newBuilder()
                .requestTime(ZonedDateTime.now())
                .source("")
                .acknowledgements("")
                .members(members)
                .build());

        assertThat(busStops).hasSize(1);
        assertThat(busStops.get(0).getId()).isEqualTo(Type.BUS_STOP.getValue());
    }

    @Test
    @DisplayName("createBusStop builds a bus stop with the correct values set for its fields")
    void createBusStop() {
        final var accuracy = 0;
        final var atcoCode = "atcoCode";
        final var description = "description";
        final var distance = 1;
        final var latitude = BigDecimal.ZERO;
        final var longitude = BigDecimal.ONE;
        final var name = "name";
        final var member = PlacesResponse.Member.newBuilder()
                .accuracy(accuracy)
                .atcoCode(atcoCode)
                .description(description)
                .distance(distance)
                .latitude(latitude)
                .longitude(longitude)
                .name(name)
                .type(Type.BUS_STOP.getValue())
                .build();

        final var busStop = busStopHelper.createBusStop(member);

        assertThat(busStop.getId()).isEqualTo(atcoCode);
        assertThat(busStop.getLatitude()).isEqualTo(latitude);
        assertThat(busStop.getLocality()).isEqualTo(description);
        assertThat(busStop.getLongitude()).isEqualTo(longitude);
        assertThat(busStop.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("a null pointer exception is thrown when createBusStop is passed a null value")
    void createBusStopThrowsNullPointerException() {
        assertThatNullPointerException().isThrownBy(() -> busStopHelper.createBusStop(null));
    }

    @TestFactory
    Stream<DynamicTest> createBusStopThrowsIllegalArgumentException() {
        return Arrays.stream(Type.values())
                .filter((final var type) -> Type.BUS_STOP != type)
                .map((final var type) -> dynamicTest(String.format("an illegal argument exception is thrown when createBusStop is passed a value with \"%s\" set for its type field", type.getValue()),
                        () -> assertThatIllegalArgumentException().isThrownBy(() ->
                                busStopHelper.createBusStop(PlacesResponse.Member.newBuilder()
                                        .accuracy(0)
                                        .latitude(BigDecimal.ZERO)
                                        .longitude(BigDecimal.ZERO)
                                        .name("")
                                        .type(type.getValue())
                                        .build()))));
    }
}