/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//*


package org.ylzl.eden.full.link.stress.testing.mongo.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.ylzl.eden.full.link.stress.testing.filter.StressContext;
import org.ylzl.eden.full.link.stress.testing.mongo.core.DynamicMongoTemplate;
import org.ylzl.eden.full.link.stress.testing.mongo.env.MongoShadowProperties;

import java.lang.reflect.Field;

*/
/**
 * MongoDB 影子库切面
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 *//*

@RequiredArgsConstructor
@Slf4j
@Aspect
public class MongoShadowAspect {

	private final MongoShadowProperties mongoShadowProperties;

	@Pointcut("@within(org.springframework.stereotype.Service) && execution(public * *(..))")
	public void pointcut() {
	}

	@Around("pointcut()")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		Object target = joinPoint.getTarget();
		Field[] fields = target.getClass().getDeclaredFields();

		boolean stress = StressContext.getContext().isStress();
		if (!stress) {
			return joinPoint.proceed();
		}

		DynamicMongoTemplate dynamicMongoTemplate = null;
		for (Field field : fields) {
			field.setAccessible(true);
			Class fieldclass = field.getType();
			if (fieldclass == MongoTemplate.class || fieldclass == DynamicMongoTemplate.class) {
				SimpleMongoClientDatabaseFactory simpleMongoClientDbFactory =
					new SimpleMongoClientDatabaseFactory(mongoShadowProperties.getUri());

				if (fieldclass == MongoTemplate.class) {
					mongoTemplate = new MultiMongoTemplate(simpleMongoClientDbFact
			try {
				result = joinPoint.proceed();
			} catch (Throwable e) {
				result = handleException(joinPoint, e);
			} finally {

			}
			return result;
		}
	}
		}
	}
*/
