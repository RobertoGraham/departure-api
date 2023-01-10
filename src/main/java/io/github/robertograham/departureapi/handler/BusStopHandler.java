package io.github.robertograham.departureapi.handler;

import io.github.robertograham.departureapi.exception.BusStopNotFoundException;
import io.github.robertograham.departureapi.exception.ProviderErrorException;
import io.github.robertograham.departureapi.response.BusStop;
import io.github.robertograham.departureapi.response.Departure;
import io.github.robertograham.departureapi.service.BusStopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@RegisterReflectionForBinding({BusStop.class, Departure.class})
public final class BusStopHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusStopHandler.class);

    private final BusStopService busStopService;

    BusStopHandler(final BusStopService busStopService) {
        this.busStopService = Objects.requireNonNull(busStopService, "busStopService cannot be null");
    }

    public Mono<ServerResponse> getNearbyBusStops(final ServerRequest serverRequest) {
        return ok()
            .contentType(APPLICATION_JSON)
            .body(busStopService.getNearbyBusStops(serverRequest.queryParam("longitude")
                    .map(BigDecimal::new)
                    .orElseThrow(), serverRequest.queryParam("latitude")
                    .map(BigDecimal::new)

                    .orElseThrow())
                .onErrorMap(ProviderErrorException.class, throwable -> {
                    LOGGER.error("Caught ProviderErrorException", throwable);
                    return new ResponseStatusException(HttpStatus.BAD_GATEWAY, throwable.getMessage());
                }), new ParameterizedTypeReference<>() {
            });
    }

    public Mono<ServerResponse> getBusStop(final ServerRequest serverRequest) {
        return ok()
            .contentType(APPLICATION_JSON)
            .body(busStopService.getBusStop(serverRequest.pathVariable("busStopId"))
                .onErrorMap(BusStopNotFoundException.class, throwable -> {
                    LOGGER.error("Caught BusStopNotFoundException", throwable);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage());
                })
                .onErrorMap(ProviderErrorException.class, throwable -> {
                    LOGGER.error("Caught ProviderErrorException", throwable);
                    return new ResponseStatusException(HttpStatus.BAD_GATEWAY, throwable.getMessage());
                }), BusStop.class);
    }

    public Mono<ServerResponse> getBusStopDepartures(final ServerRequest serverRequest) {
        return ok()
            .contentType(APPLICATION_JSON)
            .body(busStopService.getDepartures(serverRequest.pathVariable("busStopId"))
                .onErrorMap(BusStopNotFoundException.class, throwable -> {
                    LOGGER.error("Caught BusStopNotFoundException", throwable);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, throwable.getMessage());
                })
                .onErrorMap(ProviderErrorException.class, throwable -> {
                    LOGGER.error("Caught ProviderErrorException", throwable);
                    return new ResponseStatusException(HttpStatus.BAD_GATEWAY, throwable.getMessage());
                }), new ParameterizedTypeReference<>() {
            });
    }
}
