package org.ylzl.eden.spring.integration.consistencytask.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 一致性任务状态
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Getter
public enum ConsistencyTaskStatus {

	RUNNABLE(0, "就绪"),
	RUNNING(1, "执行中"),
	SUCCESS(2, "执行成功"),
	FAILED(3, "执行失败");

	private final int code;

	private final String name;
}
