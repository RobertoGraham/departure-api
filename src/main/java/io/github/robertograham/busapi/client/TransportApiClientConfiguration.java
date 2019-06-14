package io.github.robertograham.busapi.client;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

final class TransportApiClientConfiguration {

    @Bean
    private RequestInterceptor requestInterceptor(@Value("${transportApiClient.applicationId}") final String applicationId,
                                                  @Value("${transportApiClient.applicationKey}") final String applicationKey) {
        return requestTemplate -> {
            requestTemplate.query("app_id", applicationId);
            requestTemplate.query("app_key", applicationKey);
        };
    }

    @Bean
    private Logger.Level loggerLevel() {
        return Logger.Level.FULL;
    }
}