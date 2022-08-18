package org.ylzl.eden.spring.integration.businessprocess;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.spring.integration.businessprocess.executor.RollbackProcessor;
import org.ylzl.eden.spring.integration.businessprocess.process.ProcessContext;

/**
 * 锁定优惠券
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Component
public class LockCouponProcessor extends RollbackProcessor<Order> {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	protected void process(ProcessContext<Order> context) {
		Long orderNo = context.getData().getOrderNo();
		log.info("扣除优惠券, orderNo: {}", orderNo);
	}

	/**
	 * 回滚流程
	 *
	 * @param context 上下文
	 */
	@Override
	public void rollback(ProcessContext<Order> context) {
		Long orderNo = context.getData().getOrderNo();
		log.info("退回优惠券, orderNo: {}", orderNo);
	}
}
