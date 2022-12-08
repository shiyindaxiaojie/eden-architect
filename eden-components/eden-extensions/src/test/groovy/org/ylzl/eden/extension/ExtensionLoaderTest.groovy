package org.ylzl.eden.extension

import spock.lang.Specification

class ExtensionLoaderTest extends Specification {

	def "test get extension"() {
		when:
		Demo demo1 = ExtensionLoader.getExtensionLoader(Demo.class).getExtension("demo1");
		Demo demo2 = ExtensionLoader.getExtensionLoader(Demo.class).getExtension("demo2");
		Demo demo3 = ExtensionLoader.getExtensionLoader(Demo.class).getDefaultExtension();

		then:
		demo1 instanceof DemoImpl1
		demo2 instanceof DemoImpl2
		demo3 instanceof DemoImpl2
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
