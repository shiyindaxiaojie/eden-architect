package org.ylzl.eden.spring.data.redis.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.ylzl.eden.commons.collections.CollectionUtils;
import org.ylzl.eden.commons.json.JacksonUtils;
import org.ylzl.eden.commons.lang.ObjectUtils;
import org.ylzl.eden.commons.lang.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 自定义 Redis 数据仓库
 *
 * @author gyl
 * @since 0.0.1
 */
@Slf4j
@Repository
public class CommonRedisRepository implements CustomRedisRepository {

	@Autowired
	private StringRedisTemplate redisTemplate;

	/**
	 * 判断 Key 是否存在
	 *
	 * @param key
	 * @return
	 */
	@Override
	public boolean hasKey(String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 根据 key 获取 RedisObject 对象
	 *
	 * @param key   Redis 键
	 * @param clazz 目标类型
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> Optional<T> get(String key, Class<T> clazz) {
		String value = redisTemplate.opsForValue().get(key);
		return toObject(value, clazz);
	}

	/**
	 * 根据 key 设置 RedisObject 对象
	 *
	 * @param key     Redis 键
	 * @param data    目标类型
	 * @param timeout 超时（秒）
	 * @param <T>
	 */
	@Override
	public <T> void set(String key, T data, long timeout) {
		String value = null;
		try {
			value = JacksonUtils.toJSONString(data);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException("JSON 转换异常！");
		}
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 根据 key 的 hashKey 获取 RedisObject 对象
	 *
	 * @param key     Redis 键
	 * @param hashKey 哈希键
	 * @return
	 */
	@Override
	public Optional<String> hget(String key, String hashKey) {
		Object hashValue = redisTemplate.opsForHash().get(key, hashKey);
		return Optional.ofNullable(ObjectUtils.toString(hashValue));
	}

	/**
	 * 根据 key 的 hashKey 获取 RedisObject 对象
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
		return toObject(ObjectUtils.toString(hashValue), clazz);
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
	 * 获取 key 的列表
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @param <T>
	 */
	@Override
	public <T> List<T> range(String key, long start, long end, Class<T> clazz) {
		List<String> list = redisTemplate.opsForList().range(key, start, end);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		return list.stream()
			.map(
				str -> {
					try {
						return JacksonUtils.toObject(str, clazz);
					} catch (IOException e) {
						throw new RuntimeException("JSON 转换异常！");
					}
				})
			.collect(Collectors.toList());
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
			try {
				values[i] = JacksonUtils.toJSONString(data.get(i));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
				throw new RuntimeException("JSON 转换异常！");
			}
		}
		redisTemplate.opsForList().rightPushAll(key, values);
	}

	/**
	 * 添加列表到 key 的集合
	 *
	 * @param key
	 * @param data
	 * @param <T>
	 */
	public <T> void add(String key, List<T> data) {
		String[] values = new String[data.size()];
		for (int i = 0; i < values.length; i++) {
			try {
				values[i] = JacksonUtils.toJSONString(data.get(i));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
				throw new RuntimeException("JSON 转换异常！");
			}
		}
		redisTemplate.opsForSet().add(key, values);
	}

	/**
	 * 获取 key 的集合
	 *
	 * @param key
	 * @return
	 */
	@Override
	public Set<String> members(String key) {
		return redisTemplate.opsForSet().members(key);
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
	 * Redis JSON 串转化为对象
	 *
	 * @param value
	 * @param clazz
	 * @return
	 */
	@SneakyThrows
	private <T> Optional<T> toObject(String value, Class<T> clazz) {
		Optional optional = Optional.ofNullable(value).filter(StringUtils::isNotBlank);
		if (!optional.isPresent()) {
			return optional;
		}
		return Optional.of(JacksonUtils.toObject(value, clazz));
	}
}
