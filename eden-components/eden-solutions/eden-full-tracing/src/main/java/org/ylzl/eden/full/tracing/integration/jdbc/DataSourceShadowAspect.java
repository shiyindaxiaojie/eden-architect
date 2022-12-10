/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ylzl.eden.full.tracing.integration.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ylzl.eden.full.tracing.StressContext;
import org.ylzl.eden.spring.data.jdbc.datasource.routing.RoutingDataSourceContextHolder;

/**
 * DataSource 影子库切面
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@RequiredArgsConstructor
@Slf4j
@Aspect
public class DataSourceShadowAspect {

	private final String dataSourceName;

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
		RoutingDataSourceContextHolder.push(dataSourceName);
		try {
			return joinPoint.proceed();
		} finally {
			RoutingDataSourceContextHolder.poll();
		}
	}
}
