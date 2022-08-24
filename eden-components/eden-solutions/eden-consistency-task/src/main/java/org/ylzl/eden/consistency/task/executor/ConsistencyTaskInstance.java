package org.ylzl.eden.consistency.task.executor;

import lombok.*;
import lombok.experimental.Accessors;
import org.ylzl.eden.consistency.task.enums.ConsistencyTaskStatus;

/**
 * 一致性任务实例
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Data
public class ConsistencyTaskInstance {

	/**
	 * 任务ID
	 */
	private String id;

	/**
	 * 方法名
	 */
	private String methodName;

	/**
	 * 参数类型
	 */
	private String parameterTypes;

	/**
	 * 方法签名
	 */
	private String methodSignName;

	/**
	 * 任务状态
	 */
	private ConsistencyTaskStatus consistencyTaskStatus;

	/**
	 * 执行间隔
	 */
	private int executeIntervalInSeconds;

	/**
	 * 执行次数
	 */
	private int executeTimes;

	/**
	 * 是否调度执行
	 */
	private boolean isScheduleExecute;

	/**
	 * 是否异步执行
	 */
	private boolean isAsyncExecute;

	/**
	 * 延迟时间
	 */
	private int delayInSeconds;

	/**
	 * 记录执行抛出的异常信息
	 */
	private String errorMsg;

	/**
	 * 告警表达式
	 */
	private String alertExpression;

	/**
	 * 告警执行的 Bean
	 */
	private String alertBean;
}
