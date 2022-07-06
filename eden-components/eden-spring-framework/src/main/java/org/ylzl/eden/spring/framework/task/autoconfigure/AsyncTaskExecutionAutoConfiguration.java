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

package org.ylzl.eden.spring.framework.task.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.ylzl.eden.spring.framework.task.interceptor.ExceptionHandlingAsyncTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 异步任务执行器自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @see org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
 * @see org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
 * @since 2.4.x
 */
@EnableAsync
@Slf4j
@Configuration
public class AsyncTaskExecutionAutoConfiguration implements AsyncConfigurer {

	public static final String AUTOWIRED_ASYNC_TASK_EXECUTOR = "Autowired " + TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME;

	public static final int POOL_SIZE_LIMIT = Runtime.getRuntime().availableProcessors();

	public static final int QUEUE_CAPACITY_LIMIT = 10_000;

	private final TaskExecutionProperties properties;

	private final ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers;

	private final ObjectProvider<TaskDecorator> taskDecorator;

	public AsyncTaskExecutionAutoConfiguration(TaskExecutionProperties properties,
											   ObjectProvider<TaskExecutorCustomizer> taskExecutorCustomizers,
											   ObjectProvider<TaskDecorator> taskDecorator) {
		this.properties = properties;
		this.taskExecutorCustomizers = taskExecutorCustomizers;
		this.taskDecorator = taskDecorator;
	}

	@Primary
	@Bean(name = TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
	@Override
	public Executor getAsyncExecutor() {
		log.debug(AUTOWIRED_ASYNC_TASK_EXECUTOR);
		TaskExecutionProperties.Pool pool = properties.getPool();
		TaskExecutorBuilder builder = new TaskExecutorBuilder();

		if (pool.getCoreSize() > POOL_SIZE_LIMIT) {
			builder = builder.corePoolSize(POOL_SIZE_LIMIT);
		} else {
			builder = builder.corePoolSize(pool.getCoreSize());
		}

		if (pool.getMaxSize() > POOL_SIZE_LIMIT) {
			builder = builder.maxPoolSize(POOL_SIZE_LIMIT);
		} else {
			builder = builder.maxPoolSize(pool.getMaxSize());
		}

		// 注意：Spring 默认使用 LinkedBlockingQueue 无界阻塞队列
		if (pool.getQueueCapacity() == Integer.MAX_VALUE) {
			builder = builder.queueCapacity(QUEUE_CAPACITY_LIMIT);
		} else {
			builder = builder.queueCapacity(pool.getQueueCapacity());
		}

		builder = builder.allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout());
		builder = builder.keepAlive(pool.getKeepAlive());
		TaskExecutionProperties.Shutdown shutdown = properties.getShutdown();
		builder = builder.awaitTermination(shutdown.isAwaitTermination());
		builder = builder.awaitTerminationPeriod(shutdown.getAwaitTerminationPeriod());
		builder = builder.threadNamePrefix(properties.getThreadNamePrefix());
		builder = builder.customizers(taskExecutorCustomizers.orderedStream()::iterator);
		builder = builder.taskDecorator(taskDecorator.getIfUnique());

		// Spring 默认装配的 Bean 对异常的处理不是很友好，需要替换
		return new ExceptionHandlingAsyncTaskExecutor(builder.build());
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SimpleAsyncUncaughtExceptionHandler();
	}
}
