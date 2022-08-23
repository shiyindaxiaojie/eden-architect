package org.ylzl.eden.bussiness.process.choreography;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.bussiness.process.choreography.executor.RollbackProcessor;
import org.ylzl.eden.bussiness.process.choreography.process.ProcessContext;

/**
 * 保存订单
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Component
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
