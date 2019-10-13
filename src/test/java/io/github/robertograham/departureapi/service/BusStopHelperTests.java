package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.dto.PlacesResponse;
import io.github.robertograham.departureapi.client.dto.Type;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class BusStopHelperTests {

    @Test
    @DisplayName("createBusStopList filters out member objects that don't have the bus stop type")
    void createBusStopList() {
        final var members = Stream.of(Type.BUS_STOP)
            .map((final var type) -> PlacesResponse.Member.newBuilder()
                .type(type)
                .name("")
                .latitude(BigDecimal.ZERO)
                .longitude(BigDecimal.ZERO)
                .accuracy(0)
                .atcoCode(type.name())
                .build())
            .collect(Collectors.toList());

        final var busStops = BusStopHelper.createBusStopList(members);

        assertThat(busStops).hasSize(1);
        assertThat(busStops.get(0).getId()).isEqualTo(Type.BUS_STOP.name());
    }

    @Test
    @DisplayName("createBusStop builds a bus stop with the correct values set for its fields")
    void createBusStop() {
        final var member = PlacesResponse.Member.newBuilder()
            .accuracy(0)
            .atcoCode("atcoCode")
            .description("description")
            .distance(1)
            .latitude(BigDecimal.ZERO)
            .longitude(BigDecimal.ONE)
            .name("name")
            .type(Type.BUS_STOP)
            .build();

        final var busStop = BusStopHelper.createBusStop(member);

        assertThat(busStop.getId()).isEqualTo(member.getAtcoCode());
        assertThat(busStop.getLatitude()).isEqualTo(member.getLatitude());
        assertThat(busStop.getLocality()).isEqualTo(member.getDescription());
        assertThat(busStop.getLongitude()).isEqualTo(member.getLongitude());
        assertThat(busStop.getName()).isEqualTo(member.getName());
    }

    @Test
    @DisplayName("a null pointer exception is thrown when createBusStop is passed a null value")
    void createBusStopThrowsNullPointerException() {
        assertThatNullPointerException().isThrownBy(() -> BusStopHelper.createBusStop(null));
    }

    @TestFactory
    Stream<DynamicTest> createBusStopThrowsIllegalArgumentException() {
        return EnumSet.complementOf(EnumSet.of(Type.BUS_STOP)).stream()
            .map((final var type) -> dynamicTest(String.format("an illegal argument exception is thrown when createBusStop is passed a value with %s set for its type field", type.name()),
                () -> assertThatIllegalArgumentException().isThrownBy(() ->
                    BusStopHelper.createBusStop(PlacesResponse.Member.newBuilder()
                        .accuracy(0)
                        .latitude(BigDecimal.ZERO)
                        .longitude(BigDecimal.ZERO)
                        .name("")
                        .type(type)
                        .build()))));
    }

    @TestFactory
    Stream<DynamicTest> createBusStopListThrowsIllegalArgumentExceptionWhenTypeIsNotBusStop() {
        return EnumSet.complementOf(EnumSet.of(Type.BUS_STOP)).stream()
            .map((final var type) -> dynamicTest(String.format("an illegal argument exception is thrown when createBusStopList is passed a list containing a value with %s set for its type field", type.name()),
                () -> assertThatIllegalArgumentException().isThrownBy(() ->
                    BusStopHelper.createBusStopList(List.of(PlacesResponse.Member.newBuilder()
                        .accuracy(0)
                        .latitude(BigDecimal.ZERO)
                        .longitude(BigDecimal.ZERO)
                        .name("")
                        .type(type)
                        .build())))));
    }

    @Test
    @DisplayName("an illegal argument exception is thrown when createBusStopList is passed a list containing a null element")
    void createBusStopListThrowsIllegalArgumentExceptionWhenAnElementIsNull() {
        final var memberList = new ArrayList<PlacesResponse.Member>();
        memberList.add(null);
        assertThatIllegalArgumentException().isThrownBy(() ->
            BusStopHelper.createBusStopList(memberList));
    }

    @Test
    @DisplayName("a null pointer exception is thrown when createBusStopList is passed a null value")
    void createBusStopListThrowsNullPointerException() {
        assertThatNullPointerException().isThrownBy(() ->
            BusStopHelper.createBusStopList(null));
    }
}