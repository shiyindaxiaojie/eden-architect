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

package org.ylzl.eden.spring.boot.task.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Role;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.ylzl.eden.spring.boot.task.TtlThreadPoolTaskExecutor;
import org.ylzl.eden.spring.framework.task.interceptor.ExceptionHandlingAsyncTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步任务执行器自动装配
 *
 * <p>Spring Boot 3.x API 变更：TaskExecutorBuilder 和 TaskExecutorCustomizer 已移除，
 * 直接配置 ThreadPoolTaskExecutor
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
 * @see org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
 * @since 2.4.13
 */
@EnableAsync
@Slf4j
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@Configuration(proxyBeanMethods = false)
public class AsyncTaskExecutionAutoConfiguration implements AsyncConfigurer {

	public static final String AUTOWIRED_ASYNC_TASK_EXECUTOR = "Autowired ApplicationTaskExecutor";

	public static final int POOL_SIZE_LIMIT = Runtime.getRuntime().availableProcessors();

	public static final int QUEUE_CAPACITY_LIMIT = 10_000;

	private final TaskExecutionProperties properties;

	private final ObjectProvider<TaskDecorator> taskDecorator;

	public AsyncTaskExecutionAutoConfiguration(TaskExecutionProperties properties,
											   ObjectProvider<TaskDecorator> taskDecorator) {
		this.properties = properties;
		this.taskDecorator = taskDecorator;
	}

	@Primary
	@Bean(name = TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
	@Override
	public Executor getAsyncExecutor() {
		log.debug(AUTOWIRED_ASYNC_TASK_EXECUTOR);
		TaskExecutionProperties.Pool pool = properties.getPool();
		
		// 使用阿里巴巴 TTL 线程池
		ThreadPoolTaskExecutor taskExecutor = new TtlThreadPoolTaskExecutor();
		
		// 配置核心线程数
		if (pool.getCoreSize() > POOL_SIZE_LIMIT) {
			taskExecutor.setCorePoolSize(POOL_SIZE_LIMIT);
		} else {
			taskExecutor.setCorePoolSize(pool.getCoreSize());
		}

		// 配置最大线程数
		if (pool.getMaxSize() > POOL_SIZE_LIMIT) {
			taskExecutor.setMaxPoolSize(POOL_SIZE_LIMIT);
		} else {
			taskExecutor.setMaxPoolSize(pool.getMaxSize());
		}

		// 注意：Spring 默认使用 LinkedBlockingQueue 无界阻塞队列
		if (pool.getQueueCapacity() == Integer.MAX_VALUE) {
			taskExecutor.setQueueCapacity(QUEUE_CAPACITY_LIMIT);
		} else {
			taskExecutor.setQueueCapacity(pool.getQueueCapacity());
		}

		taskExecutor.setAllowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
		taskExecutor.setKeepAliveSeconds((int) pool.getKeepAlive().getSeconds());
		
		TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
		taskExecutor.setWaitForTasksToCompleteOnShutdown(shutdown.isAwaitTermination());
		if (shutdown.getAwaitTerminationPeriod() != null) {
			taskExecutor.setAwaitTerminationSeconds((int) shutdown.getAwaitTerminationPeriod().getSeconds());
		}
		
		taskExecutor.setThreadNamePrefix(properties.getThreadNamePrefix());
		
		// 设置 TaskDecorator
		TaskDecorator decorator = taskDecorator.getIfUnique();
		if (decorator != null) {
			taskExecutor.setTaskDecorator(decorator);
		}
		
		taskExecutor.initialize();
		
		// Spring 默认装配的 Bean 对异常的处理不是很友好，需要替换
		return new ExceptionHandlingAsyncTaskExecutor(taskExecutor);
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (ex, method, params) -> {
			log.error("Unexpected exception occurred invoking async method: {}", method, ex);
		};
	}
}
