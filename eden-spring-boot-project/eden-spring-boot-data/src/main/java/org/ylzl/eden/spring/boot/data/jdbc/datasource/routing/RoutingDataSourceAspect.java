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
 */

package org.ylzl.eden.spring.boot.data.jdbc.datasource.routing;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.ylzl.eden.spring.boot.data.jdbc.RoutingDataSource;

import java.lang.reflect.Method;

/**
 * 路由数据源切面
 *
 * @author gyl
 * @since 2.0.0
 */
@Slf4j
@Aspect
public class RoutingDataSourceAspect implements ApplicationContextAware {

  private static final String MSG_ROUTING_DS = "Routing dataSource to {}";

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Pointcut("within(@org.ylzl.eden.spring.boot.data.jdbc.RoutingDataSource *)")
  public void routingDataSourcePointcut() {}

  @Around("routingDataSourcePointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		String newDataSourceName = null;
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();
    if (method.isAnnotationPresent(RoutingDataSource.class)) {
      RoutingDataSource routingDataSource = method.getAnnotation(RoutingDataSource.class);
			newDataSourceName = this.getDataSourceName(routingDataSource);
    } else {
      Class declaringType = methodSignature.getDeclaringType();
      if (declaringType.isAnnotationPresent(RoutingDataSource.class)) {
        RoutingDataSource routingDataSource =
            (RoutingDataSource) declaringType.getAnnotation(RoutingDataSource.class);
				newDataSourceName = this.getDataSourceName(routingDataSource);
      }
    }

		String oldDataSourceName = null;
		DynamicRoutingDataSource dataSource = null;
    boolean isDirty = StringUtils.isNotBlank(newDataSourceName);
    if (isDirty) {
			dataSource = (DynamicRoutingDataSource) applicationContext.getBean(RoutingDataSourceDefault.BEAN_NAME);
			oldDataSourceName = dataSource.getDataSourceName();
			log.debug(MSG_ROUTING_DS, newDataSourceName);
			dataSource.setDataSourceName(newDataSourceName);
    }

    try {
      return joinPoint.proceed();
    } finally {
      if (isDirty) {
				dataSource.setDataSourceName(oldDataSourceName);
      }
    }
  }

  private String getDataSourceName(RoutingDataSource routingDataSource) {
		if (routingDataSource != null && StringUtils.isNotBlank(routingDataSource.value())) {
			return routingDataSource.value();
		}
		return null;
	}
}
