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

package org.ylzl.eden.spring.data.redis.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.spring.framework.json.support.JSONHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redis Repository 通用接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 **/
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
@Slf4j
public class CustomRedisTemplateImpl implements CustomRedisTemplate {

	private final StringRedisTemplate redisTemplate;

	/* RedisObject 操作 */

	/**
	 * 判断 Key 是否存在
	 *
	 * @param key Redis 键
	 * @return Boolean
	 */
	@Override
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 设置过期时间（秒）
	 *
	 * @param key
	 * @param timeout
	 */
	@Override
	public void expire(String key, long timeout) {
		redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 删除 Key
	 *
	 * @param key
	 */
	@Override
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/* String 操作 */

	/**
	 * 根据 key 获取值对象，并转换为 JSON
	 *
	 * @param key   Redis 键
	 * @param clazz 目标类型
	 * @param <T>   泛型
	 * @return Optional<T>
	 */
	@Override
	public <T> Optional<T> get(String key, Class<T> clazz) {
		String value = redisTemplate.opsForValue().get(key);
		return toObject(value, clazz);
	}

	/**
	 * 根据 key 获取 String 对象
	 *
	 * @param key Redis 键
	 * @return
	 */
	@Override
	public Optional<String> get(String key) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(key));
	}

	/**
	 * 根据 key 获取值对象列表，并转换为 JSON
	 *
	 * @param key   Redis 键
	 * @param clazz 目标类型
	 * @return Optional<List < T>>
	 */
	@Override
	public <T> Optional<List<T>> getForList(String key, Class<T> clazz) {
		String value = redisTemplate.opsForValue().get(key);
		if (StringUtils.isBlank(value)) {
			return Optional.empty();
		}
		return Optional.of(JSONHelper.json().parseList(value, clazz));
	}

	/**
	 * 根据 key 设置值对象
	 *
	 * @param key     Redis 键
	 * @param data    目标类型
	 * @param timeout 超时（秒）
	 * @param <T>
	 */
	@Override
	public <T> void set(String key, T data, long timeout) {
		this.set(key, data, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 根据 key 设置值对象
	 *
	 * @param key     Redis 键
	 * @param data    目标类型
	 * @param timeout 超时
	 * @param unit    单位
	 * @param <T>
	 */
	@Override
	public <T> void set(String key, T data, long timeout, TimeUnit unit) {
		String value = JSONHelper.json().toJSONString(data);
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	/* Hash 操作 */

	/**
	 * 根据 key 的 hashKey 获取值对象，并解析 JSON 对象
	 *
	 * @param key     Redis 键
	 * @param hashKey 哈希键
	 * @param clazz   目标类型
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> Optional<T> hget(String key, String hashKey, Class<T> clazz) {
		Object hashValue = redisTemplate.opsForHash().get(key, hashKey);
		return toObject(ObjectUtils.trimToString(hashValue), clazz);
	}

	/**
	 * 根据 key 的 hash 值列表，并解析为 JSON 对象
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Optional<Map<Object, Object>> hgetAll(String key) {
		Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
		return Optional.ofNullable(entries);
	}

	/**
	 * 根据 key 的 hashKey 设置 hash 值
	 *
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 */
	@Override
	public <T> void hset(String key, String hashKey, String hashValue) {
		redisTemplate.opsForHash().put(key, hashKey, hashValue);
	}

	/**
	 * 根据 key 设置多个键值对
	 *
	 * @param key
	 * @param map
	 */
	@Override
	public <T> void hset(String key, Map<?, ?> map) {
		redisTemplate.opsForHash().putAll(key, map);
	}

	/**
	 * 删除 key 的多个键值对
	 *
	 * @param key
	 * @param hashKeys
	 */
	@Override
	public void hdelete(String key, Object... hashKeys) {
		redisTemplate.opsForHash().delete(key, hashKeys);
	}

	/* List 操作 */

	/**
	 * 获取 key 的列表
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @param <T>
	 */
	@Override
	public <T> Optional<List<T>> range(String key, long start, long end, Class<T> clazz) {
		List<String> list = redisTemplate.opsForList().range(key, start, end);
		if (CollectionUtils.isEmpty(list)) {
			return Optional.empty();
		}
		return Optional.of(list.stream()
			.map(str -> JSONHelper.json().parseObject(str, clazz))
			.collect(Collectors.toList()));
	}

	/**
	 * 添加列表到 key 的列表右端
	 *
	 * @param key
	 * @param data
	 * @param <T>
	 */
	@Override
	public <T> void rightPushAll(String key, List<T> data) {
		String[] values = new String[data.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = JSONHelper.json().toJSONString(data.get(i));
		}
		redisTemplate.opsForList().rightPushAll(key, values);
	}

	/* Set 操作 */

	/**
	 * 获取 key 的集合
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Optional<Set<String>> members(String key) {
		return Optional.ofNullable(redisTemplate.opsForSet().members(key));
	}

	/**
	 * 添加列表到 key 的集合
	 *
	 * @param key
	 * @param data
	 * @param <T>
	 */
	@Override
	public <T> void add(String key, List<T> data) {
		String[] values = new String[data.size()];
		for (int i = 0; i < values.length; i++) {
			values[i] = JSONHelper.json().toJSONString(data.get(i));
		}
		redisTemplate.opsForSet().add(key, values);
	}

	/* 私有方法 */

	/**
	 * JSON 转化为对象
	 *
	 * @param value
	 * @param clazz
	 * @return Optional<T>
	 */
	private <T> Optional<T> toObject(String value, Class<T> clazz) {
		Optional optional = Optional.ofNullable(value).filter(StringUtils::isNotBlank);
		if (!optional.isPresent()) {
			return optional;
		}
		return Optional.of(JSONHelper.json().parseObject(value, clazz));
	}
}
