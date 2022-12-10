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

package org.ylzl.eden.cola.extension.core

import org.ylzl.eden.cola.extension.BizScenario
import org.ylzl.eden.cola.extension.Extension
import org.ylzl.eden.cola.extension.executor.ExtensionExecutor
import org.ylzl.eden.cola.extension.register.ExtensionRegister
import spock.lang.Specification

class ExtensionTest extends Specification {

    interface ExtensionI {

		String test()
	}

	@Extension(bizId = "A", interfaceClass = ExtensionI.class)
	class ExtensionA implements ExtensionI, Serializable {

		@Override
		String test() {
			return this.getClass().getSimpleName()
		}
	}

	@Extension(bizId = "B")
	class ExtensionB implements ExtensionI {

		@Override
		String test() {
			return this.getClass().getSimpleName()
		}
	}

	ExtensionRegister register

	ExtensionExecutor executor

	def setup() {
		register = new ExtensionRegister()
		register.registerExtension(new ExtensionA())
		register.registerExtension(new ExtensionB())

		executor = new ExtensionExecutor(register)
	}

    def "test extension"() {
		expect:

		result == executor.execute(ExtensionI.class, BizScenario.valueOf(bizId), ExtensionI::test)

		where:
		bizId   || result
		"A"     || ExtensionA.class.getSimpleName()
		"B"     || ExtensionB.class.getSimpleName()
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
