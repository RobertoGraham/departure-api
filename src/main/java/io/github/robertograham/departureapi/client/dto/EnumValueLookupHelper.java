package io.github.robertograham.departureapi.client.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class EnumValueLookupHelper {

    static <E extends Enum<E>> E fromValue(final String value, final Map<String, E> valueEnumLookup) {
        return Optional.ofNullable(valueEnumLookup.get(value))
            .orElse(null);
    }

    static <E extends Enum<E>> Map<String, E> createValueEnumLookup(final Class<E> enumClass, final Function<E, String> valueAccessor) {
        return EnumSet.allOf(enumClass).stream()
            .collect(Collectors.toMap(valueAccessor, Function.identity()));
    }
}
