package io.github.robertograham.departureapi.service;

import feign.FeignException;
import feign.FeignException.NotFound;
import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.Group;
import io.github.robertograham.departureapi.client.dto.NextBuses;
import io.github.robertograham.departureapi.client.dto.Type;
import io.github.robertograham.departureapi.exception.BusStopNotFoundException;
import io.github.robertograham.departureapi.exception.ProviderErrorException;
import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.response.Departure;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
final class BusStopServiceImpl implements BusStopService {

    private final TransportApiClient transportApiClient;

    public BusStopServiceImpl(final TransportApiClient transportApiClient) {
        this.transportApiClient = Objects.requireNonNull(transportApiClient, "transportApiClient cannot be null");
    }

    @Override
    public Mono<List<BusStop>> getNearbyBusStops(final BigDecimal longitude, final BigDecimal latitude) {
        return Mono.fromCallable(() -> transportApiClient.places(latitude, longitude, null, null, null, null, null, Type.BUS_STOP))
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorMap(FeignException.class, ProviderErrorException::new)
            .map(placesResponse -> placesResponse.members().stream()
                .filter(Objects::nonNull)
                .filter((final var member) -> Type.BUS_STOP == member.type())
                .map(BusStopHelper::createBusStop)
                .toList());
    }

    @Override
    public Mono<BusStop> getBusStop(final String busStopId) {
        return Mono.fromCallable(() -> transportApiClient.places(null, null, null, null, null, null, busStopId, Type.BUS_STOP))
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorMap(FeignException.class, ProviderErrorException::new)
            .mapNotNull(placesResponse -> placesResponse.members().stream()
                .filter(Objects::nonNull)
                .filter((final var member) -> Type.BUS_STOP == member.type())
                .filter((final var member) -> busStopId.equals(member.atcoCode()))
                .findFirst()
                .map(BusStopHelper::createBusStop)
                .orElse(null))
            .switchIfEmpty(Mono.error(new BusStopNotFoundException(busStopId)));
    }

    @Override
    public Mono<List<Departure>> getDepartures(final String busStopId) {
        return Mono.fromCallable(() -> transportApiClient.busStopDepartures(busStopId, Group.NO, 300, NextBuses.NO))
            .subscribeOn(Schedulers.boundedElastic())
            .onErrorMap(NotFound.class, throwable -> new BusStopNotFoundException(busStopId, throwable))
            .onErrorMap(FeignException.class, ProviderErrorException::new)
            .map(busStopDeparturesResponse -> busStopDeparturesResponse.departures()
                .values().stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .filter(Objects::nonNull)
                .map(DepartureHelper::createDeparture)
                .toList());

    }
}
