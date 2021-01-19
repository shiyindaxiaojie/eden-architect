package org.ylzl.eden.spring.boot.framework.aop.timer;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * 耗时统计切面
 *
 * @author gyl
 * @since 0.0.1
 */
@Aspect
public class TimerAspect {

	@Pointcut("@annotation(org.ylzl.eden.spring.boot.framework.aop.timer.Timer)")
	private void pointcut(){}

	@Around("pointcut()")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Logger log = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
		String clazzName = joinPoint.getTarget().getClass().getSimpleName();
		String methodName = joinPoint.getSignature().getName();
		long start = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long cost = System.currentTimeMillis() - start;
		log.info(
			"Timer: {}.{}() with argument[s] = {} cost {} ms", clazzName, methodName,
			Arrays.toString(joinPoint.getArgs()),
			cost);
		return result;
	}
}
