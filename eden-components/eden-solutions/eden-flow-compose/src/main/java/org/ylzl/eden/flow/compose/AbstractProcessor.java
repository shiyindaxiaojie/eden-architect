package org.ylzl.eden.flow.compose;

import org.ylzl.eden.flow.compose.context.ProcessContext;

/**
 * 抽象流程
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public abstract class AbstractProcessor<T> implements Processor<T> {

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
	public void execute(ProcessContext<T> context) {
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
	public void onException(ProcessContext<T> context, Throwable throwable) {
	}

	/**
	 * 执行流程前置处理
	 *
	 * @param context 上下文
	 */
	protected void beforeProcess(ProcessContext<T> context) {
	}

	/**
	 * 执行流程
	 *
	 * @param context 上下文
	 */
	protected void process(ProcessContext<T> context) {
	}

	/**
	 * 执行流程后置处理
	 *
	 * @param context 上下文
	 */
	protected void afterProcess(ProcessContext<T> context) {
	}
}
