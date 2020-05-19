package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.TransportForTheNorthApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.org.siri.siri20.Siri;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
final class DisruptionServiceImpl implements DisruptionService {

    private static final ZoneId ZONE_ID = ZoneId.of("Europe/London");

    private final TransportForTheNorthApiClient transportForTheNorthApiClient;
    private final String applicationId;

    DisruptionServiceImpl(final TransportForTheNorthApiClient transportForTheNorthApiClient,
                          @Value("${transportForTheNorthApiClient.applicationId}") final String applicationId) {
        this.transportForTheNorthApiClient = transportForTheNorthApiClient;
        this.applicationId = applicationId;
    }

    @Override
    public Siri getDisruptions() {
        return transportForTheNorthApiClient.situationExchange(SiriHelper.newSituationExchangeRequestBuilder()
            .currentDateTime(ZonedDateTime.now(ZONE_ID))
            .applicationId(applicationId)
            .build());
    }
}
