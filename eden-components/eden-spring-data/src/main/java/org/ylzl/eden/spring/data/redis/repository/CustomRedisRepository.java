package org.ylzl.eden.spring.data.redis.repository;

import org.apache.commons.lang3.StringUtils;
import org.ylzl.eden.commons.lang.StringConstants;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Redis 数据仓库接口
 *
 * @author gyl
 * @since 0.0.1
 */
public interface CustomRedisRepository {

  /** 默认过期时间 */
  long DEFAULT_EXPIRE = 86400L;

  /**
   * 判断 Key 是否存在
   *
   * @param key
   * @return
   */
  boolean hasKey(String key);

  /**
   * 根据 key 获取 RedisObject 对象
   *
   * @param key Redis 键
   * @param clazz 目标类型
   * @param <T>
   * @return
   */
  <T> Optional<T> get(String key, Class<T> clazz);

  /**
   * 根据 key 设置 RedisObject 对象
   *
   * @param key Redis 键
   * @param data 目标类型
   * @param timeout 超时（秒）
   * @param <T>
   */
  <T> void set(String key, T data, long timeout);

  /**
   * 根据 key 的 hashKey 获取 RedisObject 对象
   *
   * @param key Redis 键
   * @param hashKey 哈希键
   * @param <T>
   * @return
   */
  <T> Optional<T> hget(String key, String hashKey);

  /**
   * 根据 key 的 hashKey 获取 RedisObject 对象，并解析 JSON 对象
   *
   * @param key Redis 键
   * @param hashKey 哈希键
   * @param clazz 目标类型
   * @param <T>
   * @return
   */
  <T> Optional<T> hget(String key, String hashKey, Class<T> clazz);

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
   * 获取 key 的列表
   *
   * @param key
   * @param start
   * @param end
   * @param <T>
   */
  <T> List<T> range(String key, long start, long end, Class<T> clazz);

  /**
   * 添加列表到 key 的列表右端
   *
   * @param key
   * @param data
   * @param <T>
   */
  <T> void rightPushAll(String key, List<T> data);

  /**
   * 添加列表到 key 的集合
   *
   * @param key
   * @param values
   * @param <T>
   */
  <T> void add(String key, List<T> values);

  /**
   * 获取 key 的集合
   *
   * @param key
   * @return
   */
  Set<String> members(String key);

  /**
   * 设置过期时间（秒）
   *
   * @param key
   * @param timeout
   */
  void expire(String key, long timeout);

  /**
   * 根据站点 ID 构建 Redis 键
   *
   * @param keyName
   * @param siteId
   * @return
   */
  default String buildRedisKey(String keyName, int siteId) {
    return StringUtils.join(keyName, StringConstants.COLON, siteId);
  }

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

  /**
   * 根据关键字构建 Redis 键
   *
   * @param keyName
   * @param keyword
   * @return
   */
  default String buildRedisKey(String keyName, String keyword) {
    return StringUtils.join(keyName, StringConstants.COLON, keyword);
  }
}
