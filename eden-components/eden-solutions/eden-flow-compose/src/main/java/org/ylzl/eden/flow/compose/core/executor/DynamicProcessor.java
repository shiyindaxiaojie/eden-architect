package org.ylzl.eden.flow.compose.core.executor;

import org.ylzl.eden.flow.compose.core.context.ProcessContext;

/**
 * 动态流程
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public abstract class DynamicProcessor<T> extends AbstractProcessor<T> {

	/**
	 * 指定下一个节点
	 *
	 * @param context
	 * @return
	 */
	public abstract String nextNode(ProcessContext<T> context);
}
