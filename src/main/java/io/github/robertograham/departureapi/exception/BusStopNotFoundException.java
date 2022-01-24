package io.github.robertograham.departureapi.exception;

public final class BusStopNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE_FORMAT = "No bus stop found for id: %s";

    public BusStopNotFoundException(final String busStopId) {
        super(MESSAGE_FORMAT.formatted(busStopId));
    }

    public BusStopNotFoundException(final String busStopId, final Throwable cause) {
        super(MESSAGE_FORMAT.formatted(busStopId), cause);
    }
}
