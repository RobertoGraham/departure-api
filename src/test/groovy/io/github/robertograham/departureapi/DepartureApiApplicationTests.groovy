package io.github.robertograham.departureapi

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject

@Subject(DepartureApiApplication)
@SpringBootTest
@ContextConfiguration
final class DepartureApiApplicationTests extends Specification {

    def "application starts"() {
        when: "the application starts"

        then:
        noExceptionThrown()
    }
}
