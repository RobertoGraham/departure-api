package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.TransportApiClient;
import io.github.robertograham.departureapi.client.dto.BusServiceResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
final class BusServiceServiceImpl implements BusServiceService {

    @NonNull
    private final TransportApiClient transportApiClient;

    @Override
    public BusServiceResponse getBusService(final String operatorCode, final String line) {
        return transportApiClient.busService(operatorCode, line);
    }
}
