package org.ylzl.eden.consistency.task.alerter;

import org.ylzl.eden.consistency.task.executor.ConsistencyTaskInstance;

/**
 * 一致性任务告警器
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface ConsistencyTaskAlerter {

	/**
	 * 发送告警
	 *
	 * @param instance
	 */
	void sendAlert(ConsistencyTaskInstance instance);
}
