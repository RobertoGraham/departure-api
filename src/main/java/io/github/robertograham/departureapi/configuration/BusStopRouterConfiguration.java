package io.github.robertograham.departureapi.configuration;

import io.github.robertograham.departureapi.handler.BusStopHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BusStopRouterConfiguration {

    @Bean
    RouterFunction<ServerResponse> busStopRouterFunction(final BusStopHandler busStopHandler) {
        return route()
            .GET("/busStops", accept(APPLICATION_JSON), busStopHandler::getNearbyBusStops)
            .GET("/busStops/{busStopId}", accept(APPLICATION_JSON), busStopHandler::getBusStop)
            .GET("/{busStopId}/departures", accept(APPLICATION_JSON), busStopHandler::getBusStopDepartures)
            .build();
    }
}
