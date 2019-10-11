package io.github.robertograham.departureapi.client.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

final class TypeTests {

    @TestFactory
    Stream<DynamicTest> fromValue() {
        return Arrays.stream(Type.values())
            .map((final var type) -> dynamicTest(String.format("\"%s\" is mapped to %s", type.getValue(), type.name()),
                () -> assertThat(Type.fromValue(type.getValue())).isEqualTo(type)));
    }

    @Test
    @DisplayName("fromValue throws IllegalArgumentException when value has no mapping")
    void fromValueUnmappedValue() {
        assertThatIllegalArgumentException().isThrownBy(() -> Type.fromValue(null));
        assertThatIllegalArgumentException().isThrownBy(() -> Type.fromValue("unmappedValue"));
    }
}
