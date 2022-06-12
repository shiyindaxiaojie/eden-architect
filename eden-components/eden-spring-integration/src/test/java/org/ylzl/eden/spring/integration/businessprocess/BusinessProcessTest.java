package org.ylzl.eden.spring.integration.businessprocess;

import org.junit.Test;
import org.ylzl.eden.commons.id.SnowflakeGenerator;
import org.ylzl.eden.spring.integration.businessprocess.config.parser.ClassPathXmlProcessParser;
import org.ylzl.eden.spring.integration.businessprocess.process.ProcessContext;

/**
 * 业务流程测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class BusinessProcessTest {

	@Test
	public void assertThatClassPathXml() {
		ProcessEngine processEngine = new ProcessEngine(new ClassPathXmlProcessParser("/businessprocess/process.xml"));
		ProcessContext<Order> processContext = processEngine.getContext("order");
		processContext.setData(Order.builder()
			.orderNo(SnowflakeGenerator.builder().dataCenterId(1L).workerId(1L).build().nextId())
			.stock(1000).build());
		processContext.start();
		Order order = processContext.getData();
		System.out.println(order.getStock());
	}
}
