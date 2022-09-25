package org.ylzl.eden.bpm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.bpm.executor.StandardProcessor;
import org.ylzl.eden.bpm.process.ProcessContext;

/**
 * 风控检查
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Component
public class RiskCheckProcessor extends StandardProcessor<Order> {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	protected void process(ProcessContext<Order> context) {
		Long orderNo = context.getData().getOrderNo();
		log.info("风控检查, orderNo: {}", orderNo);
	}
}
