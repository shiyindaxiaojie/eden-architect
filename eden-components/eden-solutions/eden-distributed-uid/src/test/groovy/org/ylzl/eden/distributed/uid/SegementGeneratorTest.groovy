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

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.ylzl.eden.distributed.uid.exception.IdGeneratorException
import org.ylzl.eden.distributed.uid.support.SegmentGeneratorHelper
import spock.lang.Specification

import javax.sql.DataSource

class SegementGeneratorTest extends Specification {

	DataSource dataSource

	def setup() {
		dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
	}

	def cleanup() {

	}

	def "test next id"() {
		given:
		SegmentGenerator segmentGenerator = SegmentGeneratorHelper.segmentGenerator("leaf", dataSource)

		when:
		int id = 0
		for (i in 0..< 1000) {
			id += segmentGenerator.nextId()
		}

		then:
		id == 500500
		notThrown(IdGeneratorException)
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
