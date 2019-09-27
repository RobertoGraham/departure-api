package io.github.robertograham.departureapi.service;

import io.github.robertograham.departureapi.client.dto.BusServiceResponse;

public interface BusServiceService {

    BusServiceResponse getBusService(String operatorCode, String line);
}
