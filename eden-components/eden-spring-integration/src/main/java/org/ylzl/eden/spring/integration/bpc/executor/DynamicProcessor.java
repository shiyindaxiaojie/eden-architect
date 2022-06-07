package org.ylzl.eden.spring.integration.bpc.executor;

import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;

/**
 * 动态流程
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class DynamicProcessor extends AbstractProcessor {

	/**
	 * 指定下一个节点
	 *
	 * @param context
	 * @return
	 */
	public abstract String nextNode(ProcessContext context);
}
