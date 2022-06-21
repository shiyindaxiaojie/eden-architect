package org.ylzl.eden.integration.tests

import spock.lang.*

class TestTest extends Specification {
    Test test = new Test()

    def "test assert That Success"() {
        when:
        boolean result = test.assertThatSuccess()

        then:
        result == true
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
