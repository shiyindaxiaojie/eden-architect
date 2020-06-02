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
import org.ylzl.eden.spring.boot.data.jdbc.RoutingDataSource;
import org.ylzl.eden.spring.boot.data.jdbc.datasource.DataSourceNameHolder;

import java.lang.reflect.Method;

/**
 * 路由数据源切面
 *
 * @author gyl
 * @since 2.0.0
 */
@Slf4j
@Aspect
public class RoutingDataSourceAspect {

  private static final String MSG_ROUTING_DS = "Routing dataSource to {}";

  @Pointcut("@annotation(org.ylzl.eden.spring.boot.data.jdbc.RoutingDataSource)")
  public void routingDataSourcePointcut() {}

  @Around("routingDataSourcePointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    boolean isDirty = false;
    String oldDataSourceName = DataSourceNameHolder.get();
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();
    if (method.isAnnotationPresent(RoutingDataSource.class)) {
      RoutingDataSource routingDataSource = method.getAnnotation(RoutingDataSource.class);
      isDirty = this.setDataSource(routingDataSource);
    } else {
      Class declaringType = methodSignature.getDeclaringType();
      if (declaringType.isAnnotationPresent(RoutingDataSource.class)) {
        RoutingDataSource routingDataSource =
            (RoutingDataSource) declaringType.getAnnotation(RoutingDataSource.class);
        isDirty = this.setDataSource(routingDataSource);
      }
    }

    try {
      return joinPoint.proceed();
    } finally {
      if (isDirty) {
        DataSourceNameHolder.set(oldDataSourceName);
      }
    }
  }

  private boolean setDataSource(RoutingDataSource routingDataSource) {
    if (routingDataSource != null && StringUtils.isNotBlank(routingDataSource.value())) {
      DataSourceNameHolder.set(routingDataSource.value());
      log.debug(MSG_ROUTING_DS, routingDataSource.value());
      return true;
    }
    return false;
  }
}
