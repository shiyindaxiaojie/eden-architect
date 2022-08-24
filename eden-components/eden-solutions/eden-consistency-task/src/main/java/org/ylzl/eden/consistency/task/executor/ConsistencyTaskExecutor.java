package org.ylzl.eden.consistency.task.executor;

/**
 * 一致性任务执行器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface ConsistencyTaskExecutor {

	/**
	 * 执行
	 *
	 * @param instance
	 */
	void execute(ConsistencyTaskInstance instance);

	/**
	 * 回滚
	 *
	 * @param instance
	 */
	void fallback(ConsistencyTaskInstance instance);
}
