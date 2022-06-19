package org.ylzl.eden.spring.test.redis

import spock.lang.*

class EmbeddedRedisTest extends Specification {
    //Field redisServer of type RedisServer - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
	@Shared
	EmbeddedRedis embeddedRedis = new EmbeddedRedis(6379)

    def "test before"() {
        when:
        embeddedRedis.before()

        then:
		true
    }

    def "test is Open"() {
        when:
        boolean result = embeddedRedis.isOpen()

        then:
		result
    }

	def "test after"() {
		when:
		embeddedRedis.after()

		then:
		true
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
