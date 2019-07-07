package io.github.robertograham.busapi.service;

import io.github.robertograham.busapi.client.TransportApiClient;
import io.github.robertograham.busapi.client.dto.BusServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
final class BusServiceServiceImpl implements BusServiceService {

    private final TransportApiClient transportApiClient;

    @Autowired
    BusServiceServiceImpl(final TransportApiClient transportApiClient) {
        this.transportApiClient = transportApiClient;
    }

    @Override
    public BusServiceResponse getBusService(final String operatorCode, final String line) {
        return transportApiClient.busService(operatorCode, line);
    }
}
