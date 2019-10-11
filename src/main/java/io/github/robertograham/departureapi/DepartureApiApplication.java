package io.github.robertograham.departureapi;

import io.github.robertograham.departureapi.client.TransportApiClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {TransportApiClient.class})
@SpringBootApplication
public class DepartureApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartureApiApplication.class, args);
    }
}
