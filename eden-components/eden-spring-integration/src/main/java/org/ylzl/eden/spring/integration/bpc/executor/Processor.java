package org.ylzl.eden.spring.integration.bpc.executor;

import org.ylzl.eden.spring.integration.bpc.node.ProcessContext;

/**
 * 流程执行器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public interface Processor {

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	void execute(ProcessContext context);

	/**
	 * 异常处理
	 *
	 * @param context   上下文
	 * @param throwable 异常
	 */
	void onException(ProcessContext context, Throwable throwable);

	/**
	 * 设置流程名称
	 *
	 * @param name 流程名称
	 */
	void setName(String name);

	/**
	 * 获取流程名称
	 *
	 * @return 流程名称
	 */
	String getName();
}
