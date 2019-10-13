package io.github.robertograham.departureapi.client.dto

import spock.lang.Specification
import spock.lang.Unroll

final class BearingTests extends Specification {

    @Unroll
    def "\"#bearing.getValue()\" is mapped to #bearing.name()"() {
        expect:
        Bearing.fromValue((bearing as Bearing).getValue()) == bearing

        where:
        bearing << Bearing.values()
    }

    @Unroll
    def "fromValue throws IllegalArgumentException when value has no mapping"() {
        when:
        Bearing.fromValue(value)

        then:
        thrown(IllegalArgumentException)

        where:
        value << ["unmappedValue", null]
    }
}
