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

package org.ylzl.eden.flow.compose.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.flow.compose.RollbackProcessor;
import org.ylzl.eden.flow.compose.context.ProcessContext;
import org.ylzl.eden.flow.compose.model.Order;

/**
 * 保存订单
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Component("saveOrderProcessor")
public class SaveOrderProcessor extends RollbackProcessor<Order> {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	protected void process(ProcessContext<Order> context) {
		Long orderNo = context.getData().getOrderNo();
		log.info("保存订单, orderNo: {}", orderNo);
		context.getVariables("orderNo").toString(); // 故意报错
	}

	/**
	 * 回滚流程
	 *
	 * @param context 上下文
	 */
	@Override
	public void rollback(ProcessContext<Order> context) {
		Long orderNo = context.getData().getOrderNo();
		log.info("保存订单失败，回滚订单数据, orderNo: {}", orderNo);
	}
}
