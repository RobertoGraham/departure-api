package io.github.robertograham.departureapi

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.Subject

@Subject(DepartureApiApplication)
@SpringBootTest
final class DepartureApiApplicationTests extends Specification {

    def "application starts"() {
        when: "the application starts"

        then:
        noExceptionThrown()
    }
}