package org.ylzl.eden.spring.integration.businessprocess

import groovy.transform.CompileStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@CompileStatic
class BusinessProcessTestTest {

	private BusinessProcessTest businessProcessTestUnderTest

	@BeforeEach
	void setUp() {
		businessProcessTestUnderTest = new BusinessProcessTest()
	}

	@Test
	void testAssertThatClassPathXml() {
		// Setup
		// Run the test
		businessProcessTestUnderTest.assertThatClassPathXml()

		// Verify the results
	}
}
