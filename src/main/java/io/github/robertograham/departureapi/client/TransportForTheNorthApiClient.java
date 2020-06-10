package io.github.robertograham.departureapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import uk.org.siri.siri20.Siri;

@FeignClient(
    name = "transportForTheNorthApiClient",
    url = "${transportForTheNorthApiClient.url}",
    configuration = TransportForTheNorthApiClientConfiguration.class
)
public interface TransportForTheNorthApiClient {

    @PostMapping(value = "/siri/sx", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    Siri situationExchange(Siri siri);
}
