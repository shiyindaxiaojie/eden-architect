package org.ylzl.eden.spring.integration.bpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ylzl.eden.spring.integration.bpc.executor.RollbackProcessor;
import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;

/**
 * 锁定库存
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Slf4j
@Component
public class DeductStockProcessor extends RollbackProcessor {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	protected void process(ProcessContext context) {
		Long orderNo = context.get("orderNo");
		Integer stock = context.get("stock");
		stock--;
		log.info("锁定库存, orderNo: {}, stock: {}", orderNo, stock);
	}

	/**
	 * 回滚流程
	 *
	 * @param context 上下文
	 */
	@Override
	public void rollback(ProcessContext context) {
		Long orderNo = context.get("orderNo");
		Integer stock = context.get("stock");
		stock++;
		log.info("释放库存, orderNo: {}, stock: {}", orderNo, stock);
	}
}
