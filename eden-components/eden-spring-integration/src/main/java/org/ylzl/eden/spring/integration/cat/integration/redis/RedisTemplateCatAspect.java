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

package org.ylzl.eden.spring.integration.cat.integration.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.ylzl.eden.spring.integration.cat.integration.redis.connection.RedisClusterConnectionWrapper;
import org.ylzl.eden.spring.integration.cat.integration.redis.connection.RedisConnectionWrapper;

/**
 * Redis 切入 CAT 埋点
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@Aspect
public class RedisTemplateCatAspect {

	@Pointcut("target(org.springframework.data.redis.connection.RedisConnectionFactory)")
	public void connectionFactory() {
	}

	@Pointcut("execution(org.springframework.data.redis.connection.RedisConnection *.getConnection(..))")
	public void getConnection() {
	}

	@Pointcut("execution(org.springframework.data.redis.connection.RedisClusterConnection *.getClusterConnection(..))")
	public void getClusterConnection() {
	}

	@Around("getConnection() && connectionFactory()")
	public Object aroundGetConnection(final ProceedingJoinPoint pjp) throws Throwable {
		RedisConnection connection = (RedisConnection) pjp.proceed();
		return new RedisConnectionWrapper(connection);
	}

	@Around("getClusterConnection() && connectionFactory()")
	public Object aroundGetClusterConnection(final ProceedingJoinPoint pjp) throws Throwable {
		RedisClusterConnection clusterConnection = (RedisClusterConnection) pjp.proceed();
		return new RedisClusterConnectionWrapper(clusterConnection);
	}
}
