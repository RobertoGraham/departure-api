package io.github.robertograham.busapi.client.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Stops {

    ALL("all"), ONWARD("onward");

    @Getter
    @NonNull
    private final String value;
}
