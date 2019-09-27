package io.github.robertograham.departureapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DepartureApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepartureApiApplication.class, args);
    }
}
