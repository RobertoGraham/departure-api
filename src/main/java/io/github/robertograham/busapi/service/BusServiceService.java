package io.github.robertograham.busapi.service;

import io.github.robertograham.busapi.client.dto.BusServiceResponse;

public interface BusServiceService {

    BusServiceResponse getBusService(String operatorCode, String line);
}
