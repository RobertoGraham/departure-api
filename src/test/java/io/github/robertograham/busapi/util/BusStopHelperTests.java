package io.github.robertograham.busapi.util;

import io.github.robertograham.busapi.client.dto.PlacesResponse;
import io.github.robertograham.busapi.client.dto.Type;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class BusStopHelperTests {

    private BusStopHelper busStopHelper;

    @BeforeEach
    void setUp() {
        busStopHelper = new BusStopHelper();
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
                .filter(type -> Type.BUS_STOP != type)
                .map(type -> dynamicTest(String.format("an illegal argument exception is thrown when createBusStop is passed a value with \"%s\" set for its type field", type.getValue()),
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