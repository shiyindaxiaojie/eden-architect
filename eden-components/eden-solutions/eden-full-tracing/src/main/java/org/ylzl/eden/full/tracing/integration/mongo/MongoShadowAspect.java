package org.ylzl.eden.full.tracing.integration.mongo;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.ylzl.eden.full.tracing.context.StressContext;
import org.ylzl.eden.spring.data.mongodb.core.MongoDatabaseSelector;

/**
 * MongoDB 影子库切面
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Slf4j
@Aspect
public class MongoShadowAspect {

	private final MongoDatabaseFactory shadowMongoDatabaseFactory;

	public MongoShadowAspect(MongoDatabaseFactory shadowMongoDatabaseFactory) {
		this.shadowMongoDatabaseFactory = shadowMongoDatabaseFactory;
	}

	@Pointcut("@within(org.springframework.stereotype.Repository) && execution(public * *(..))")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		// 判断是否启用压测
		boolean stress = StressContext.getContext().isStress();
		if (!stress) {
			return joinPoint.proceed();
		}

		// 切换影子库执行
		MongoDatabaseSelector.set(shadowMongoDatabaseFactory);
		try {
			return joinPoint.proceed();
		} finally {
			MongoDatabaseSelector.remove();
		}
	}
}
