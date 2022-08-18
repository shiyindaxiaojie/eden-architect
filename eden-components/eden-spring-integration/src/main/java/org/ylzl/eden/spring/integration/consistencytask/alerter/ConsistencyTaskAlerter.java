package org.ylzl.eden.spring.integration.consistencytask.alerter;

import org.ylzl.eden.spring.integration.consistencytask.executor.ConsistencyTaskInstance;

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
