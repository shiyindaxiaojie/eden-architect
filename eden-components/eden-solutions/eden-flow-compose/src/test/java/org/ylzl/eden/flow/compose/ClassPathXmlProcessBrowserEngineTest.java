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
public class ClassPathXmlProcessBrowserEngineTest {

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
