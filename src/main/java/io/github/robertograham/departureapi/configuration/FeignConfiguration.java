package io.github.robertograham.departureapi.configuration;

import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.TransportForTheNorthApiClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@EnableFeignClients(clients = {TransportApiClient.class, TransportForTheNorthApiClient.class})
class FeignConfiguration {
}