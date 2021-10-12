package io.github.robertograham.departureapi.controller;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
final class FeignExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(FeignExceptionHandler.class);

    @ExceptionHandler(FeignException.class)
    private void handleFeignException(final HttpServletResponse httpServletResponse,
                                      final FeignException feignException) throws IOException {
        log.error("Caught FeignException", feignException);
        httpServletResponse.sendError(HttpStatus.BAD_GATEWAY.value(), "Communication error occurred with data provider");
    }
}
