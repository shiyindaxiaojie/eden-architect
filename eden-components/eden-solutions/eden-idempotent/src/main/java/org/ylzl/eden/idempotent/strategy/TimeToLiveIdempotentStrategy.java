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

package org.ylzl.eden.idempotent.strategy;

import java.util.concurrent.TimeUnit;

/**
 * 基于过期策略管理幂等请求
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public interface TimeToLiveIdempotentStrategy {

	/**
	 * 检查是否一次请求
	 *
	 * @param key      键
	 * @param value    值
	 * @param ttl      存活时间
	 * @param timeUnit 时间单位
	 */
	void checkOnceRequest(String key, String value, long ttl, TimeUnit timeUnit);
}
