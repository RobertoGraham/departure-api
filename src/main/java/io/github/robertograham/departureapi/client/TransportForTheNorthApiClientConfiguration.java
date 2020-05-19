package io.github.robertograham.departureapi.client;

import feign.auth.BasicAuthRequestInterceptor;
import feign.jaxb.JAXBContextFactory;
import feign.jaxb.JAXBDecoder;
import feign.jaxb.JAXBEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.nio.charset.StandardCharsets;

final class TransportForTheNorthApiClientConfiguration {

    @Bean
    private BasicAuthRequestInterceptor requestInterceptor(@Value("${transportForTheNorthApiClient.applicationId}") final String applicationId,
                                                           @Value("${transportForTheNorthApiClient.applicationKey}") final String applicationKey) {
        return new BasicAuthRequestInterceptor(applicationId, applicationKey, StandardCharsets.UTF_8);
    }

    @Bean
    private JAXBContextFactory jaxbContextFactory() {
        return new JAXBContextFactory.Builder()
            .withMarshallerJAXBEncoding(StandardCharsets.UTF_8.displayName())
            .withMarshallerSchemaLocation("http://www.siri.org.uk/siri http://www.siri.org.uk/schema/2.0/xsd/siri.xsd")
            .build();
    }

    @Bean
    private JAXBEncoder encoder(final JAXBContextFactory jaxbContextFactory) {
        return new JAXBEncoder(jaxbContextFactory);
    }

    @Bean
    private JAXBDecoder decoder(final JAXBContextFactory jaxbContextFactory) {
        return new JAXBDecoder(jaxbContextFactory);
    }
}