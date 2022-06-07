package org.ylzl.eden.spring.integration.bpc.executor;

import org.ylzl.eden.spring.integration.bpc.process.ProcessContext;

/**
 * 抽象流程
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public abstract class AbstractProcessor implements Processor {

	private String name;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	@Override
	public void execute(ProcessContext context) {
		beforeProcess(context);
		process(context);
		afterProcess(context);
	}

	/**
	 * 异常处理
	 *
	 * @param context   上下文
	 * @param throwable 异常
	 */
	@Override
	public void onException(ProcessContext context, Throwable throwable) {}

	/**
	 * 执行流程前置处理
	 *
	 * @param context 上下文
	 */
	protected void beforeProcess(ProcessContext context) {}

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	protected void process(ProcessContext context) {}

	/**
	 * 执行流程后置处理
	 *
	 * @param context 上下文
	 */
	protected void afterProcess(ProcessContext context) {}
}
