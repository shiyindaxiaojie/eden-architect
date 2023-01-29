/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.spring.test.embedded.redis


import spock.lang.Shared
import spock.lang.Specification

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
