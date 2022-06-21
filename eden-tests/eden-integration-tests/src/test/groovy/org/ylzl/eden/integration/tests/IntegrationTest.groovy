package org.ylzl.eden.integration.tests

import spock.lang.*

class IntegrationTest extends Specification {
    Integration integration = new Integration()

    def "integration assert That Success"() {
        when:
        boolean result = integration.assertThatSuccess()

        then:
		result
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
