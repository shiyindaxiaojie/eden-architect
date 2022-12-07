package org.ylzl.eden.flow.compose;

import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.ylzl.eden.commons.id.SnowflakeGenerator;
import org.ylzl.eden.flow.compose.context.ProcessContext;
import org.ylzl.eden.flow.compose.context.ProcessContextFactory;
import org.ylzl.eden.flow.compose.model.Order;

/**
 * 业务流程测试
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
//@ComponentScan(basePackageClasses = SpringInjectProcessEngineTest.class)
//@SpringJUnitConfig(classes = OrderConfiguartion.class)
public class SpringInjectProcessEngineTest {

	@Autowired
	private ProcessContextFactory processContextFactory;

//	@Test
	public void assertThatSpringBean() {
		ProcessContext<Order> processContext = processContextFactory.getContext("order");
		long orderNo = SnowflakeGenerator.nextId(1, 1);
		int stock = 1000;
		processContext.setData(Order.builder().orderNo(orderNo).stock(stock).build());
		processContext.start();
		Order order = processContext.getData();
		Assertions.assertEquals(orderNo, order.getOrderNo());
		Assertions.assertEquals(stock, order.getStock());
	}
}
