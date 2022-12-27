package io.github.robertograham.departureapi.exception;

public class ProviderErrorException extends RuntimeException {

    public ProviderErrorException(final Throwable throwable) {
        super("Communication error occurred with data provider", throwable);
    }
}
