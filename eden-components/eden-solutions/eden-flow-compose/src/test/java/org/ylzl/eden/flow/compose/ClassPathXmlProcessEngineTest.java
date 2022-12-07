package org.ylzl.eden.flow.compose;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.ylzl.eden.commons.id.SnowflakeGenerator;
import org.ylzl.eden.flow.compose.context.ProcessContext;
import org.ylzl.eden.flow.compose.model.Order;
import org.ylzl.eden.flow.compose.parser.ClassPathXmlProcessParser;

/**
 * 业务流程测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class ClassPathXmlProcessEngineTest {

	@Test
	public void assertThatClassPathXml() {
		ProcessEngine processEngine = new ProcessEngine(new ClassPathXmlProcessParser("/META-INF/process.xml"));
		ProcessContext<Order> processContext = processEngine.getContext("order");
		long orderNo = SnowflakeGenerator.nextId(1, 1);
		int stock = 1000;
		processContext.setData(Order.builder().orderNo(orderNo).stock(stock).build());
		processContext.start();
		Order order = processContext.getData();
		Assertions.assertEquals(orderNo, order.getOrderNo());
		Assertions.assertEquals(stock, order.getStock());
	}
}
