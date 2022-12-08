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
