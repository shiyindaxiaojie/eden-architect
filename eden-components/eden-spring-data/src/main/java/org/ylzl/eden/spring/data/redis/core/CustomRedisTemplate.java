package org.ylzl.eden.spring.data.redis.core;

import org.apache.commons.lang3.StringUtils;
import org.ylzl.eden.commons.lang.StringConstants;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 自定义 Redis 模板接口
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 **/
public interface CustomRedisTemplate {

	/**
	 * 默认过期时间（一天）
	 */
	long DEFAULT_EXPIRE = 86400L;

	/* RedisObject 操作 */

	/**
	 * 判断 Key 是否存在
	 *
	 * @param key
	 * @return
	 */
	boolean hasKey(String key);

	/**
	 * 删除 Key
	 *
	 * @param key
	 */
	void delete(String key);

	/**
	 * 设置过期时间（秒），注意原子性操作
	 *
	 * @param key
	 * @param timeout
	 */
	void expire(String key, long timeout);

	/* String 操作 */

	/**
	 * 根据 key 获取值对象，并解析为 JSON 对象
	 *
	 * @param key   Redis 键
	 * @param clazz 目标类型
	 * @param <T>
	 * @return
	 */
	<T> Optional<T> get(String key, Class<T> clazz);

	/**
	 * 根据 key 获取 String 对象
	 *
	 * @param key   Redis 键
	 * @return
	 */
	Optional<String> get(String key);

	/**
	 * 根据 key 获取值对象列表，并解析为 JSON 对象列表
	 *
	 * @param key   Redis 键
	 * @param clazz 目标类型
	 * @param <T>
	 * @return
	 */
	<T> Optional<List<T>> getForList(String key, Class<T> clazz);

	/**
	 * 根据 key 设置值对象（set ex 原子性操作）
	 *
	 * @param key     Redis 键
	 * @param data    目标类型
	 * @param timeout 超时（秒）
	 * @param <T>
	 */
	<T> void set(String key, T data, long timeout);

	/**
	 * 根据 key 设置值对象
	 *
	 * @param key     Redis 键
	 * @param data    目标类型
	 * @param timeout 超时
	 * @param unit    单位
	 * @param <T>
	 */
	<T> void set(String key, T data, long timeout, TimeUnit unit);

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
	<T> Optional<T> hget(String key, String hashKey, Class<T> clazz);

	/**
	 * 根据 key 的 hash 值列表，并解析为 JSON 对象
	 *
	 * @param key
	 * @return
	 */
	Optional<Map<Object, Object>> hgetAll(String key);

	/**
	 * 根据 key 的 hashKey 设置 hash 值
	 *
	 * @param key
	 * @param hashKey
	 * @param hashValue
	 * @param <T>
	 */
	<T> void hset(String key, String hashKey, String hashValue);

	/**
	 * 根据 key 设置多个键值对
	 *
	 * @param key
	 * @param map
	 * @param <T>
	 */
	<T> void hset(String key, Map<?, ?> map);

	/**
	 * 删除 key 的多个键值对
	 *
	 * @param key
	 * @param hashKeys
	 */
	void hdelete(String key, Object... hashKeys);

	/* List 操作 */

	/**
	 * 获取 key 的列表
	 *
	 * @param key
	 * @param start
	 * @param end
	 * @param <T>
	 */
	<T> Optional<List<T>> range(String key, long start, long end, Class<T> clazz);

	/**
	 * 添加列表到 key 的列表右端
	 *
	 * @param key
	 * @param data
	 * @param <T>
	 */
	<T> void rightPushAll(String key, List<T> data);

	/* Set 操作 */

	/**
	 * 获取 key 的集合
	 *
	 * @param key
	 * @return
	 */
	Optional<Set<String>> members(String key);

	/**
	 * 添加列表到 key 的集合
	 *
	 * @param key
	 * @param values
	 * @param <T>
	 */
	<T> void add(String key, List<T> values);

	/**
	 * 根据 ID 构建 Redis 键
	 *
	 * @param keyName
	 * @param id
	 * @return
	 */
	default String buildRedisKey(String keyName, long id) {
		return StringUtils.join(keyName, StringConstants.COLON, id);
	}
}
