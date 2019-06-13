package io.github.robertograham.busapi.configuration;

import io.bitbucket.robertograham.transportapi.TransportApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TransportApiConfiguration {

    @Bean
    TransportApi transportApi(@Value("${transportApi.applicationId}") final String applicationId,
                              @Value("${transportApi.applicationKey}") final String applicationKey) {
        return TransportApi.newTransportApi(applicationId, applicationKey);
    }
}
