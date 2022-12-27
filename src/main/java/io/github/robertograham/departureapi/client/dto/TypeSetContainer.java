package io.github.robertograham.departureapi.client.dto;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record TypeSetContainer(Set<Type> types) {

    public TypeSetContainer(Type... types) {
        this(Arrays.stream(types)
            .collect(Collectors.toSet()));
    }
}
