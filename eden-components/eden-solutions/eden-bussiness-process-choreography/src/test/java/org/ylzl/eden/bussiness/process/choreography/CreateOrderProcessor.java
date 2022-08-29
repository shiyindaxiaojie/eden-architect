package org.ylzl.eden.bussiness.process.choreography;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.bussiness.process.choreography.executor.StandardProcessor;
import org.ylzl.eden.bussiness.process.choreography.process.ProcessContext;

/**
 * 创建订单
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Component
public class CreateOrderProcessor extends StandardProcessor<Order> {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	protected void process(ProcessContext<Order> context) {
		Long orderNo = context.getData().getOrderNo();
		log.info("创建订单, orderNo: {}", orderNo);
	}
}