package io.github.robertograham.departureapi.controller;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestControllerAdvice
final class FeignExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FeignException.class)
    private void handleFeignException(final HttpServletResponse httpServletResponse,
                                      final FeignException feignException) throws IOException {
        log.error("Caught FeignException", feignException);
        httpServletResponse.sendError(HttpStatus.BAD_GATEWAY.value(), "Communication error occurred with data provider");
    }
}
