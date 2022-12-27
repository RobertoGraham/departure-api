package io.github.robertograham.departureapi.configuration;

import io.github.robertograham.departureapi.handler.BusRouteHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BusRouteRouterConfiguration {

    @Bean
    RouterFunction<ServerResponse> busRouteRouterFunction(final BusRouteHandler busRouteHandler) {
        return route()
            .GET("/busRoutes/{operator}/{line}", accept(APPLICATION_JSON), busRouteHandler::getBusRoute)
            .build();
    }
}
