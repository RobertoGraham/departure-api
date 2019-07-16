package io.github.robertograham.busapi.controller;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
final class ControllerAdvice {

    @ExceptionHandler({FeignException.class})
    private ResponseEntity handleFeignException(final FeignException feignException) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(String.format("Received %d status code when sending a request to Transport API", feignException.status()));
    }
}
