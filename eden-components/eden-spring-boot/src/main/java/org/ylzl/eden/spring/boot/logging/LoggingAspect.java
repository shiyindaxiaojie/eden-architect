package org.ylzl.eden.spring.boot.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;

/**
 * TODO
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class LoggingAspect {

	private static final AntPathMatcher pathMatcher = new AntPathMatcher();

	private final LoggingAspectProperties loggingAspectProperties;

	@Around("@within(org.ylzl.eden.spring.boot.logging.Logging) || " +
		"@annotation(org.ylzl.eden.spring.boot.logging.Logging)")
	public Object logAround(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
		// 判断是否开启日志切面
		if (!loggingAspectProperties.isEnabled()) {
			return joinPoint.proceed();
		}

		StringBuilder logBuilder = new StringBuilder();

		// 判断是否需要输出日志
		if (!shouldLog(logging.sampleRate())) {
			return joinPoint.proceed();
		}

		// 判断是否需要输出该类的日志
		String className = joinPoint.getTarget().getClass().getName();
		if (!isLogging(className, logging.value())) {
			return joinPoint.proceed();
		}

		// 记录类名和方法名
		logBuilder.append("Class ").append(className);
		logBuilder.append(", method ").append(joinPoint.getSignature().getName()).append(": ");

		// 记录方法入参
		if (loggingAspectProperties.isLogArgs()) {
			Object[] args = joinPoint.getArgs();
			if (args != null && args.length > 0) {
				logBuilder.append("arguments=").append(Arrays.toString(args)).append("; ");
			}
		}

		// 记录方法执行耗时
		if (loggingAspectProperties.isLogExecutionTime()) {
			long start = System.currentTimeMillis();
			Object result = joinPoint.proceed();
			long end = System.currentTimeMillis();
			logBuilder.append("execution time=").append(end - start).append("ms; ");
			returnResult(joinPoint, result, logBuilder);
		} else {
			return joinPoint.proceed();
		}

		return null;
	}

	/**
	 * 判断是否需要输出日志，根据sampleRate属性和Math.random()方法计算得出
	 */
	private boolean shouldLog(double sampleRate) {
		return sampleRate >= 1.0 || Math.random() < sampleRate;
	}

	/**
	 * 判断是否需要输出该类的日志
	 */
	private boolean isLogging(String className, String[] packages) {
		if (packages == null || packages.length == 0) {
			return false;
		}

		for (String pkg : packages) {
			if (pathMatcher.match(pkg, className)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 记录方法返回值
	 */
	private void returnResult(JoinPoint joinPoint, Object result, StringBuilder logBuilder) {
		if (loggingAspectProperties.isLogReturnValue()) {
			logBuilder.append("return value=").append(result).append("; ");
		}
		log.info(logBuilder.toString());
	}
}

