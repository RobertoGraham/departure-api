package io.github.robertograham.departureapi.controller;

import feign.FeignException;
import io.github.robertograham.departureapi.exception.BusStopNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
final class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(FeignException.class)
    private void handleFeignException(final HttpServletResponse httpServletResponse, final FeignException feignException) throws IOException {
        log.error("Caught FeignException", feignException);
        httpServletResponse.sendError(HttpStatus.BAD_GATEWAY.value(), "Communication error occurred with data provider");
    }

    @ExceptionHandler(BusStopNotFoundException.class)
    private void handleBusStopNotFoundException(final HttpServletResponse httpServletResponse, final BusStopNotFoundException busStopNotFoundException) throws IOException {
        log.error("Caught BusStopNotFoundException", busStopNotFoundException);
        httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), busStopNotFoundException.getMessage());
    }
}
