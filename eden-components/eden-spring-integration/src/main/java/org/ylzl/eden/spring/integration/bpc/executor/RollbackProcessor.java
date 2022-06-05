package org.ylzl.eden.spring.integration.bpc.executor;

import org.ylzl.eden.spring.integration.bpc.node.ProcessContext;

/**
 * 可回滚的流程
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class RollbackProcessor extends AbstractProcessor {

	/**
	 * 回滚流程
	 *
	 * @param context 上下文
	 */
	public abstract void rollback(ProcessContext context);
}
