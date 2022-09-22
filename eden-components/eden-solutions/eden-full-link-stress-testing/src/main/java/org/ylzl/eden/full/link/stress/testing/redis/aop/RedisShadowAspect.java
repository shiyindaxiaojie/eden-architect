package org.ylzl.eden.full.link.stress.testing.redis.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ylzl.eden.full.link.stress.testing.filter.StressContext;
import org.ylzl.eden.full.link.stress.testing.redis.env.RedisShadowProperties;
import org.ylzl.eden.spring.data.redis.core.RedisDatabaseSelector;

/**
 * Redis 影子库切面
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Aspect
public class RedisShadowAspect {

	private final RedisShadowProperties redisShadowProperties;

	@Pointcut("@within(org.springframework.stereotype.Repository) && execution(public * *(..))")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 是否启用压测
		boolean stress = StressContext.getContext().isStress();
		if (!stress) {
			return joinPoint.proceed();
		}

		// 切换影子库执行
		RedisDatabaseSelector.set(redisShadowProperties.getDatabase());
		try {
			return joinPoint.proceed();
		} finally {
			RedisDatabaseSelector.remove();
		}
	}
}
