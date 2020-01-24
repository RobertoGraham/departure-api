package io.github.robertograham.departureapi.controller

import io.github.robertograham.departureapi.client.TransportApiClient
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import spock.mock.DetachedMockFactory

@TestConfiguration
class TransportApiClientStubConfiguration {

    private final def detachedMockFactory = new DetachedMockFactory()

    @Bean
    TransportApiClient transportApiClient() {
        return detachedMockFactory.Stub(TransportApiClient)
    }
}
