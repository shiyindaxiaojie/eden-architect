package org.ylzl.eden.spring.integration.bpc;

import org.junit.Test;
import org.ylzl.eden.commons.id.SnowflakeGenerator;
import org.ylzl.eden.spring.integration.bpc.config.parser.ClassPathXmlProcessParser;
import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class BusinessProcessTest {

	@Test
	public void assertThat() {
		ProcessEngine processEngine = new ProcessEngine(new ClassPathXmlProcessParser("/bpc/process.xml"));
		ProcessContext processContext = processEngine.getContext("order");
		processContext.set("orderNo", SnowflakeGenerator.builder().dataCenterId(1L).workerId(1L).build().nextId());
		processContext.set("stock", 1000);
		processContext.start();
	}
}
