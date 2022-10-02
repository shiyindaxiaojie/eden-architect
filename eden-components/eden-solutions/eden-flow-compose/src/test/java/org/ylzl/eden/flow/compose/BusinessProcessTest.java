package org.ylzl.eden.flow.compose;

import org.junit.jupiter.api.Test;
import org.ylzl.eden.flow.compose.config.parser.ClassPathXmlProcessParser;
import org.ylzl.eden.flow.compose.process.ProcessContext;
import org.ylzl.eden.commons.id.SnowflakeGenerator;

/**
 * 业务流程测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class BusinessProcessTest {

	@Test
	public void assertThatClassPathXml() {
		ProcessEngine processEngine = new ProcessEngine(new ClassPathXmlProcessParser("/META-INF/process.xml"));
		ProcessContext<Order> processContext = processEngine.getContext("order");
		processContext.setData(Order.builder()
			.orderNo(SnowflakeGenerator.builder().dataCenterId(1L).workerId(1L).build().nextId())
			.stock(1000).build());
		processContext.start();
		Order order = processContext.getData();
		System.out.println(order.getStock());
	}
}
