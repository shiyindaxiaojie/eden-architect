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

package org.ylzl.eden.distributed.uid

import org.apache.zookeeper.server.embedded.ZooKeeperServerEmbedded
import org.ylzl.eden.distributed.uid.exception.IdGeneratorException
import org.ylzl.eden.distributed.uid.integration.leaf.snowflake.model.App
import spock.lang.Specification
import org.ylzl.eden.distributed.uid.support.IdGeneratorHelper

class IdGeneratorTest extends Specification {

	ZooKeeperServerEmbedded zooKeeperServer

	def setup() {
		zooKeeperServer = ZooKeeperServerEmbedded.builder().build()
		zooKeeperServer.start()
	}

	def cleanup() {
		zooKeeperServer.close()
	}

	def "test next id"() {
		when:
		println IdGeneratorHelper.idGenerator("leaf", App.builder().port(8080).build()).nextId()

		then:
		notThrown(IdGeneratorException)
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
