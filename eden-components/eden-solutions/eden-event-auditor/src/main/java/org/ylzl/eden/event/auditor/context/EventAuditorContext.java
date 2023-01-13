package org.ylzl.eden.event.auditor.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import java.util.Deque;
import java.util.Map;

/**
 * 事件审计上下文
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class EventAuditorContext {

	private static final TransmittableThreadLocal<Deque<Map<String, Object>>> VARIABLES =
		new TransmittableThreadLocal<Deque<Map<String, Object>>>() {

			@Override
			protected Deque<Map<String, Object>> initialValue() {
				return Queues.newArrayDeque();
			}
		};

	private static final TransmittableThreadLocal<Deque<Map<String, Object>>> GLOBAL_VARIABLES =
		new TransmittableThreadLocal<Deque<Map<String, Object>>>() {

			@Override
			protected Deque<Map<String, Object>> initialValue() {
				return Queues.newArrayDeque();
			}
		};

	public static void putVariable(String key, Object value) {
		put(key, value, VARIABLES);
	}

	public static void putGlobalVariable(String key, Object value) {
		put(key, value, GLOBAL_VARIABLES);
	}

	public static Object getVariable(String key) {
		return get(key, VARIABLES);
	}

	public static Object getGlobalVariable(String key) {
		return get(key, GLOBAL_VARIABLES);
	}

	public static Map<String, Object> getVariables() {
		return getVariables(VARIABLES);
	}

	public static Map<String, Object> getGlobalVariables() {
		return getVariables(GLOBAL_VARIABLES);
	}

	public static void clearVariables() {
		VARIABLES.remove();
	}

	public static void clearGlobalVariables() {
		GLOBAL_VARIABLES.remove();
	}

	public static void clear() {
		clearVariables();
		clearGlobalVariables();
	}

	private static void put(String key, Object value, TransmittableThreadLocal<Deque<Map<String, Object>>> context) {
		Deque<Map<String, Object>> variables = context.get();
		if (variables.isEmpty()) {
			variables.push(Maps.newHashMap());
		}
		variables.element().put(key, value);
	}

	private static Object get(String key, TransmittableThreadLocal<Deque<Map<String, Object>>> context) {
		Map<String, Object> variables = context.get().peek();
		return variables != null? variables.get(key) : null;
	}

	private static Map<String, Object> getVariables(TransmittableThreadLocal<Deque<Map<String, Object>>> context) {
		return context.get().peek();
	}
}
