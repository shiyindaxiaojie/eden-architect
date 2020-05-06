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

package org.ylzl.eden.spring.boot.integration.javamelody;

import lombok.extern.slf4j.Slf4j;
import net.bull.javamelody.*;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.Pointcuts;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * 监控自动配置
 *
 * @author gyl
 * @since 0.0.1
 */
@ConditionalOnClass(Main.class)
@ConditionalOnWebApplication
@ConditionalOnProperty(
    prefix = JavaMelodyConstants.PROP_PREFIX,
    name = "enabled",
    matchIfMissing = true)
@EnableConfigurationProperties(JavaMelodyProperties.class)
@Slf4j
@Configuration
public class JavaMelodyAutoConfiguration {

  public static final String BEAN_DEFAULT_NAME = JavaMelodyConstants.PROP_PREFIX;

  public static final String BEAN_FILTER_NAME = JavaMelodyConstants.PROP_PREFIX + "-registration";

  @Bean
  public ServletListenerRegistrationBean<EventListener> monitoringSessionListener(
      ServletContext servletContext) {
    EventListener eventListener = new SessionListener();
    final ServletListenerRegistrationBean<EventListener> servletListenerRegistrationBean =
        new ServletListenerRegistrationBean<>(eventListener);
    if (servletContext.getFilterRegistration(JavaMelodyConstants.PROP_PREFIX) != null) {
      servletListenerRegistrationBean.setEnabled(false);
    }
    return servletListenerRegistrationBean;
  }

  @Bean(name = BEAN_FILTER_NAME)
  @ConditionalOnMissingBean(name = BEAN_FILTER_NAME)
  public FilterRegistrationBean monitoringFilter(
      JavaMelodyProperties properties, ServletContext servletContext) {
    final FilterRegistrationBean registrationBean = new FilterRegistrationBean();

    final MonitoringFilter filter;
    if (properties.isManagementEndpointMonitoringEnabled()) {
      filter =
          new MonitoringFilter() {
            @Override
            protected boolean isAllowed(HttpServletRequest request, HttpServletResponse response)
                throws IOException {
              response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden access");
              return false;
            }
          };
    } else {
      filter = new MonitoringFilter();
    }
    filter.setApplicationType("Spring Boot");

    registrationBean.setFilter(filter);
    registrationBean.setAsyncSupported(true);
    registrationBean.setName(BEAN_DEFAULT_NAME);
    registrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);

    for (final Entry<String, String> parameter : properties.getInitParameters().entrySet()) {
      registrationBean.addInitParameter(parameter.getKey(), parameter.getValue());
    }

    registrationBean.addUrlPatterns("/*");

    final FilterRegistration filterRegistration =
        servletContext.getFilterRegistration(BEAN_DEFAULT_NAME);
    if (filterRegistration != null) {
      registrationBean.setEnabled(false);
      for (final Entry<String, String> entry : registrationBean.getInitParameters().entrySet()) {
        filterRegistration.setInitParameter(entry.getKey(), entry.getValue());
      }
    }
    return registrationBean;
  }

  @Bean
  @ConditionalOnMissingBean(DefaultAdvisorAutoProxyCreator.class)
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "advisor-auto-proxy-creator-enabled",
      matchIfMissing = false)
  public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
    return new DefaultAdvisorAutoProxyCreator();
  }

  @Bean
  public SpringDataSourceBeanPostProcessor monitoringDataSourceBeanPostProcessor(
      @Value("${" + JavaMelodyConstants.PROP_PREFIX + ".excluded-datasources:}")
          String excludedDatasources) {
    final SpringDataSourceBeanPostProcessor processor = new SpringDataSourceBeanPostProcessor();
    if (excludedDatasources != null && excludedDatasources.trim().length() > 0) {
      processor.setExcludedDatasources(
          new HashSet<>(Arrays.asList(excludedDatasources.split(","))));
    }
    return processor;
  }

  @Bean
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "spring-monitoring-enabled",
      matchIfMissing = true)
  public MonitoringSpringAdvisor monitoringSpringAdvisor() {
    return new MonitoringSpringAdvisor(new MonitoredWithAnnotationPointcut());
  }

  @Bean
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "spring-monitoring-enabled",
      matchIfMissing = true)
  public MonitoringSpringAdvisor monitoringSpringServiceAdvisor() {
    return new MonitoringSpringAdvisor(new AnnotationMatchingPointcut(Service.class));
  }

  @Bean
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "spring-monitoring-enabled",
      matchIfMissing = true)
  public MonitoringSpringAdvisor monitoringSpringControllerAdvisor() {
    return new MonitoringSpringAdvisor(new AnnotationMatchingPointcut(Controller.class));
  }

  @Bean
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "spring-monitoring-enabled",
      matchIfMissing = true)
  public MonitoringSpringAdvisor monitoringSpringRestControllerAdvisor() {
    return new MonitoringSpringAdvisor(new AnnotationMatchingPointcut(RestController.class));
  }

  @Bean
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "spring-monitoring-enabled",
      matchIfMissing = true)
  public MonitoringSpringAdvisor monitoringSpringAsyncAdvisor() {
    return new MonitoringSpringAdvisor(
        Pointcuts.union(
            new AnnotationMatchingPointcut(Async.class),
            new AnnotationMatchingPointcut(null, Async.class)));
  }

  @Bean
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "scheduled-monitoring-enabled",
      matchIfMissing = true)
  @ConditionalOnMissingBean(DefaultAdvisorAutoProxyCreator.class)
  public MonitoringSpringAdvisor monitoringSpringScheduledAdvisor() {
    return new MonitoringSpringAdvisor(
        Pointcuts.union(
            new AnnotationMatchingPointcut(null, Scheduled.class),
            new AnnotationMatchingPointcut(null, Schedules.class)));
  }

  @SuppressWarnings("unchecked")
  @Bean
  @ConditionalOnClass(name = "org.springframework.cloud.netflix.feign.FeignClient")
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "spring-monitoring-enabled",
      matchIfMissing = true)
  @ConditionalOnMissingBean(DefaultAdvisorAutoProxyCreator.class)
  public MonitoringSpringAdvisor monitoringFeignClientAdvisor() throws ClassNotFoundException {
    final Class<? extends Annotation> feignClientClass =
        (Class<? extends Annotation>)
            Class.forName("org.springframework.cloud.netflix.feign.FeignClient");
    final MonitoringSpringAdvisor advisor =
        new MonitoringSpringAdvisor(new AnnotationMatchingPointcut(feignClientClass, true));
    advisor.setAdvice(
        new MonitoringSpringInterceptor() {
          private static final long serialVersionUID = 1L;

          @Override
          protected String getRequestName(MethodInvocation invocation) {
            final StringBuilder sb = new StringBuilder();
            final Method method = invocation.getMethod();
            final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping != null) {
              String[] path = requestMapping.value();
              if (path.length == 0) {
                path = requestMapping.path();
              }
              if (path.length > 0) {
                sb.append(path[0]);
                sb.append(' ');
                if (requestMapping.method().length > 0) {
                  sb.append(requestMapping.method()[0].name());
                } else {
                  sb.append("GET");
                }
                sb.append('\n');
              }
            }
            final Class<?> declaringClass = method.getDeclaringClass();
            final String classPart = declaringClass.getSimpleName();
            final String methodPart = method.getName();
            sb.append(classPart).append('.').append(methodPart);
            return sb.toString();
          }
        });
    return advisor;
  }

  @Bean
  @ConditionalOnProperty(
      prefix = JavaMelodyConstants.PROP_PREFIX,
      name = "spring-monitoring-enabled",
      matchIfMissing = true)
  public SpringRestTemplateBeanPostProcessor monitoringRestTemplateBeanPostProcessor() {
    return new SpringRestTemplateBeanPostProcessor();
  }

  @Bean
  public SpringContext javamelodySpringContext() {
    return new SpringContext();
  }
}
