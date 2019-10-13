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

final class BearingTests {

    @TestFactory
    Stream<DynamicTest> fromValue() {
        return Arrays.stream(Bearing.values())
            .map((final var bearing) -> dynamicTest(String.format("\"%s\" is mapped to %s", bearing.getValue(), bearing.name()),
                () -> assertThat(Bearing.fromValue(bearing.getValue())).isEqualTo(bearing)));
    }

    @Test
    @DisplayName("fromValue throws IllegalArgumentException when value has no mapping")
    void fromValueUnmappedValue() {
        assertThatIllegalArgumentException().isThrownBy(() -> Bearing.fromValue(null));
        assertThatIllegalArgumentException().isThrownBy(() -> Bearing.fromValue("unmappedValue"));
    }
}
