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

package org.ylzl.eden.spring.integration.cat.tracing;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.dianping.cat.Cat;
import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Objects;

/**
 * 链路上下文
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class TraceContext implements Cat.Context {

	public static final String TRACE_ID = "traceId";

	private static final TransmittableThreadLocal<Cat.Context> holder = new TransmittableThreadLocal<>();

	private final Map<String, String> properties = Maps.newHashMap();

	private static final String UUID_KEY = "_catUUIDKey";

	@Override
	public void addProperty(String key, String value) {
		if (key == null || value == null) {
			return;
		}

		((TraceContext) getContext()).properties.put(key, value);
	}

	@Override
	public String getProperty(String key) {
		if (key == null) {
			return null;
		}

		return ((TraceContext) getContext()).properties.get(key);
	}

	public static String getTraceId() {
		return ((TraceContext) getContext()).properties.get(Cat.Context.ROOT);
	}

	public static Cat.Context getContext(String uuid) {
		Cat.Context context = holder.get();
		if (context == null) {
			context = new TraceContext();
			if (uuid != null) {
				context.addProperty(UUID_KEY, uuid);
			}
			holder.set(context);
		}
		return context;
	}

	public static Cat.Context getContext() {
		return getContext(null);
	}

	public static void remove(String uuid) {
		if (uuid == null) {
			remove();
		}
		Cat.Context context = holder.get();
		if (context != null) {
			String uuidKey = context.getProperty(UUID_KEY);
			if (Objects.equals(uuid, uuidKey)) {
				remove();
			}
		}
	}

	public static void remove() {
		if (holder.get() != null) {
			holder.remove();
		}
	}
}
