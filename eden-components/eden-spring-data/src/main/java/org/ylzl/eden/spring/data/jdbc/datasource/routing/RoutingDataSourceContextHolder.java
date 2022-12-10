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
package org.ylzl.eden.spring.data.jdbc.datasource.routing;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;
import org.ylzl.eden.commons.lang.StringUtils;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 动态路由数据源上下文管理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@UtilityClass
public class RoutingDataSourceContextHolder {

	/**
	 * 使用双向队列支持 A -> B -> C 数据源嵌套+线程池
	 */
	private static final TransmittableThreadLocal<Deque<String>> CONTEXT = new TransmittableThreadLocal<Deque<String>>() {

		@Override
		protected Deque<String> initialValue() {
			return new ArrayDeque<>();
		}
	};

	/**
	 * 获得当前线程数据源
	 *
	 * @return 数据源名称
	 */
	public static String peek() {
		return CONTEXT.get().peek();
	}

	/**
	 * 设置当前线程数据源
	 * <p>
	 * 如非必要不要手动调用，调用后确保最终清除
	 * </p>
	 *
	 * @param ds 数据源名称
	 */
	public static String push(String ds) {
		String dataSourceStr = StringUtils.isEmpty(ds) ? "" : ds;
		CONTEXT.get().push(dataSourceStr);
		return dataSourceStr;
	}

	/**
	 * 清空当前线程数据源
	 * <p>
	 * 如果当前线程是连续切换数据源 只会移除掉当前线程的数据源名称
	 * </p>
	 */
	public static void poll() {
		Deque<String> deque = CONTEXT.get();
		deque.poll();
		if (deque.isEmpty()) {
			CONTEXT.remove();
		}
	}

	/**
	 * 强制清空本地线程
	 * <p>
	 * 防止内存泄漏，如手动调用了push可调用此方法确保清除
	 * </p>
	 */
	public static void clear() {
		CONTEXT.remove();
	}
}
