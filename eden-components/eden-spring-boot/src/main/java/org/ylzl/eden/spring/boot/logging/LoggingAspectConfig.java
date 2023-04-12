package org.ylzl.eden.spring.boot.logging;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 日志切面配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class LoggingAspectConfig {

	/**
	 * 是否开启日志切面
	 */
	private boolean enabled = true;

	/**
	 * 需要输出日志的包名
	 */
	private String[] packages = {};

	/**
	 * 日志输出采样率
	 */
	private double sampleRate = 1.0;

	/**
	 * 是否输出入参
	 */
	private boolean logArgs = true;

	/**
	 * 是否输出返回值
	 */
	private boolean logReturnValue = true;

	/**
	 * 是否输出方法执行耗时
	 */
	private boolean logExecutionTime = true;
}
