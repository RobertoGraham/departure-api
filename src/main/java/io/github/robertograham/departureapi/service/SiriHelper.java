package io.github.robertograham.departureapi.service;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import uk.org.siri.siri20.RequestorRef;
import uk.org.siri.siri20.ServiceRequest;
import uk.org.siri.siri20.Siri;
import uk.org.siri.siri20.SituationExchangeRequestStructure;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
final class SiriHelper {


    @Builder(builderClassName = "SituationExchangeRequestBuilder", builderMethodName = "newSituationExchangeRequestBuilder")
    private static Siri createSituationExchangeRequest(@NonNull final ZonedDateTime currentDateTime,
                                                       @NonNull final String applicationId) {
        final var currentDateTimeUtc = currentDateTime.withZoneSameInstant(ZoneOffset.UTC).withNano(0);
        final var siri = new Siri();
        final var serviceRequest = new ServiceRequest();
        serviceRequest.setRequestTimestamp(currentDateTimeUtc);
        final var requestorRef = new RequestorRef();
        requestorRef.setValue(applicationId);
        serviceRequest.setRequestorRef(requestorRef);
        final var situationExchangeRequest = new SituationExchangeRequestStructure();
        situationExchangeRequest.setRequestTimestamp(currentDateTimeUtc);
        situationExchangeRequest.setVersion("2.0");
        serviceRequest.getSituationExchangeRequests().add(situationExchangeRequest);
        siri.setServiceRequest(serviceRequest);
        return siri;
    }
}
