package io.github.robertograham.busapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BusApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusApiApplication.class, args);
    }
}
