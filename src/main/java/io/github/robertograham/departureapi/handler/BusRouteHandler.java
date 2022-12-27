package io.github.robertograham.departureapi.handler;

import io.github.robertograham.departureapi.exception.ProviderErrorException;
import io.github.robertograham.departureapi.service.BusRouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
public final class BusRouteHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusRouteHandler.class);

    private final BusRouteService busRouteService;

    BusRouteHandler(final BusRouteService busRouteService) {
        this.busRouteService = Objects.requireNonNull(busRouteService, "busRouteService cannot be null");
    }

    public Mono<ServerResponse> getBusRoute(final ServerRequest serverRequest) {
        return ok()
            .contentType(APPLICATION_JSON)
            .body(busRouteService.getBusRoute(serverRequest.pathVariable("operator"), serverRequest.pathVariable("line"), serverRequest.queryParam("busStopId")
                        .orElseThrow(), serverRequest.queryParam("direction")
                        .orElseThrow(), serverRequest.queryParam("epochSecond")
                        .map(Long::parseLong)
                        .orElseThrow())
                    .onErrorMap(ProviderErrorException.class, throwable -> {
                        LOGGER.error("Caught ProviderErrorException", throwable);
                        return new ResponseStatusException(HttpStatus.BAD_GATEWAY, throwable.getMessage());
                    })
                , new ParameterizedTypeReference<>() {
                });
    }
}
