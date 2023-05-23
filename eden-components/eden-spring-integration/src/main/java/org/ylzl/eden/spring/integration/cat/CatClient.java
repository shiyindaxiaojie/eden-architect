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

package org.ylzl.eden.spring.integration.cat;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.spring.integration.cat.analyzer.MetricTagAggregator;

import java.util.Map;
import java.util.function.Supplier;

/**
 * CAT 客户端工具
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@UtilityClass
public class CatClient {

	/**
	 * Transaction 埋点
	 *
	 * @param type     类型
	 * @param name     指标
	 * @param data     附加数据
	 * @param function Lambda 函数
	 * @param <T>      泛型
	 * @return 函数返回值
	 */
	public static <T> T newTransaction(String type, String name, Map<String, Object> data, Supplier<T> function) {
		Transaction transaction = Cat.newTransaction(type, name);
		try {
			if (data != null && !data.isEmpty()) {
				data.forEach(transaction::addData);
			}
			T result = function.get();
			transaction.setStatus(Message.SUCCESS);
			return result;
		} catch (Exception e) {
			Cat.logError(e);
			if (e.getMessage() != null) {
				Cat.logEvent(type + "." + name + ".Error", e.getMessage());
			}
			transaction.setStatus(e);
			throw new RuntimeException(e);
		} finally {
			transaction.complete();
		}
	}

	/**
	 * Transaction 埋点
	 *
	 * @param type     类型
	 * @param name     指标
	 * @param function Lambda 函数
	 * @param <T>      泛型
	 * @return 函数返回值
	 */
	public static <T> T newTransaction(String type, String name, Supplier<T> function) {
		return newTransaction(type, name, null, function);
	}

	/**
	 * Transaction 埋点
	 *
	 * @param type     类型
	 * @param name     指标
	 * @param data     附加数据
	 * @param runnable 异步函数
	 */
	public static void newTransaction(String type, String name, Map<String, Object> data, Runnable runnable) {
		Transaction transaction = Cat.newTransaction(type, name);
		try {
			if (data != null && !data.isEmpty()) {
				data.forEach(transaction::addData);
			}
			runnable.run();
			transaction.setStatus(Message.SUCCESS);
		} catch (Exception e) {
			Cat.logError(e.getMessage(), e);
			if (e.getMessage() != null) {
				Cat.logEvent(type + "." + name + ".Error", e.getMessage());
			}
			transaction.setStatus(e);
			throw new RuntimeException(e);
		} finally {
			transaction.complete();
		}
	}

	/**
	 * Transaction 埋点
	 *
	 * @param type     类型
	 * @param name     指标
	 * @param runnable 异步函数
	 */
	public static void newTransaction(String type, String name, Runnable runnable) {
		newTransaction(type, name, null, runnable);
	}

	/**
	 * Event 埋点
	 *
	 * @param type 类型
	 * @param name 指标
	 */
	public static void logEvent(String type, String name) {
		Cat.logEvent(type, name);
	}

	/**
	 * Event 埋点
	 *
	 * @param type           类型
	 * @param name           指标
	 * @param status         状态（Event.SUCCESS 表示成功）
	 * @param nameValuePairs 键值对
	 * @see com.dianping.cat.message.Event#SUCCESS
	 */
	public static void logEvent(String type, String name, String status, String nameValuePairs) {
		Cat.logEvent(type, name, status, nameValuePairs);
	}

	/**
	 * Problem 埋点
	 *
	 * @param message 错误信息
	 * @param cause   异常堆栈
	 */
	public static void logError(String message, Throwable cause) {
		Cat.logError(message, cause);
	}

	/**
	 * Problem 埋点
	 *
	 * @param cause 异常堆栈
	 */
	public static void logError(Throwable cause) {
		Cat.logError(cause);
	}

	/**
	 * Heartbeat 埋点
	 *
	 * @param type           类型
	 * @param name           指标
	 * @param status         状态（Event.SUCCESS 表示成功）
	 * @param nameValuePairs 键值对
	 * @see com.dianping.cat.message.Event#SUCCESS
	 */
	public static void logHeartbeat(String type, String name, String status, String nameValuePairs) {
		Cat.logHeartbeat(type, name, status, nameValuePairs);
	}

	/**
	 * 业务指标埋点
	 *
	 * @param name 指标
	 */
	public static void logMetricForCount(String name) {
		Cat.logMetricForCount(name);
	}

	/**
	 * 业务指标埋点
	 *
	 * @param name     指标
	 * @param quantity 数量
	 */
	public static void logMetricForCount(String name, int quantity) {
		Cat.logMetricForCount(name, quantity);
	}

	/**
	 * 业务指标埋点
	 *
	 * @param name 指标
	 * @param tags 标签
	 */
	public static void logMetricForCount(String name, Map<String, String> tags) {
		if (CollectionUtils.isEmpty(tags)) {
			logMetricForCount(name, 1);
			return;
		}
		MetricTagAggregator.getInstance().addCountMetric(name, 1, tags);
	}

	/**
	 * 业务指标埋点
	 *
	 * @param name     指标
	 * @param quantity 数量
	 * @param tags     标签
	 */
	public static void logMetricForCount(String name, int quantity, Map<String, String> tags) {
		if (CollectionUtils.isEmpty(tags)) {
			logMetricForCount(name, quantity);
			return;
		}
		MetricTagAggregator.getInstance().addCountMetric(name, quantity, tags);
	}

	/**
	 * 业务指标埋点
	 *
	 * @param name             指标
	 * @param durationInMillis 耗时（毫秒）
	 */
	public static void logMetricForDuration(String name, long durationInMillis) {
		Cat.logMetricForDuration(name, durationInMillis);
	}

	/**
	 * 业务指标埋点
	 *
	 * @param name             指标
	 * @param durationInMillis 耗时（毫秒）
	 * @param tags             标签
	 */
	public static void logMetricForDuration(String name, long durationInMillis, Map<String, String> tags) {
		if (CollectionUtils.isEmpty(tags)) {
			logMetricForDuration(name, durationInMillis);
			return;
		}
		MetricTagAggregator.getInstance().addTimerMetric(name, durationInMillis, tags);
	}

	/**
	 * 业务指标埋点
	 *
	 * @param name 指标
	 * @param sum  总和
	 */
	public static void logMetricForSum(String name, double sum) {
		Cat.logMetricForSum(name, sum);
	}

	/**
	 * 业务指标埋点
	 *
	 * @param name     指标
	 * @param sum      总和
	 * @param quantity 数量
	 */
	public static void logMetricForSum(String name, double sum, int quantity) {
		Cat.logMetricForSum(name, sum, quantity);
	}
}
