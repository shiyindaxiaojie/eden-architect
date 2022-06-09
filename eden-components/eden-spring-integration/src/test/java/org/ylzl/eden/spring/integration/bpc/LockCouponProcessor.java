package org.ylzl.eden.spring.integration.bpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.spring.integration.bpc.executor.RollbackProcessor;
import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;

/**
 * 锁定优惠券
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@Component
public class LockCouponProcessor extends RollbackProcessor {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	protected void process(ProcessContext context) {
		Long orderNo = context.get("orderNo");
		log.info("锁定优惠券, orderNo: {}", orderNo);
	}

	/**
	 * 回滚流程
	 *
	 * @param context 上下文
	 */
	@Override
	public void rollback(ProcessContext context) {
		Long orderNo = context.get("orderNo");
		log.info("退回优惠券, orderNo: {}", orderNo);
	}
}
