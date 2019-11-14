package io.github.robertograham.departureapi.client.dto

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Subject([Bearing, EnumValueLookupHelper])
final class BearingTests extends Specification {

    @Unroll
    def "\"#bearing.getValue()\" is mapped to Bearing.#bearing.name()"() {
        expect:
        Bearing.fromValue((bearing as Bearing).getValue()) == bearing

        where:
        bearing << Bearing.values()
    }

    @Unroll
    def "fromValue returns null when value has no mapping"() {
        expect:
        Bearing.fromValue(value) == null

        where:
        value << ["unmappedValue", null]
    }
}
