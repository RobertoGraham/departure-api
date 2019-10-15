package io.github.robertograham.departureapi.client.dto

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

@Subject([Type, EnumValueLookupHelper])
final class TypeTests extends Specification {

    @Unroll
    def "\"#type.getValue()\" is mapped to Type.#type.name()"() {
        expect:
        Type.fromValue((type as Type).getValue()) == type

        where:
        type << Type.values()
    }

    @Unroll
    def "fromValue throws IllegalArgumentException when value has no mapping"() {
        when:
        Type.fromValue(value)

        then:
        thrown(IllegalArgumentException)

        where:
        value << ["unmappedValue", null]
    }
}
