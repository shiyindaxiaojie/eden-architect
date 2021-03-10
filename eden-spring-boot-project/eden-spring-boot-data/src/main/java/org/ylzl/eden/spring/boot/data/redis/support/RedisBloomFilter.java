package org.ylzl.eden.spring.boot.data.redis.support;

import com.google.common.hash.Funnel;
import org.springframework.data.redis.core.RedisTemplate;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Redis 布隆过滤器
 *
 * @author gyl
 * @since 1.0.0
 */
public class RedisBloomFilter<T> {

  private final int numHashFunctions; // Hash 函数数量，不能超过 255 个
  private final Funnel<? super T> funnel; // 使用 java.nio.ByteBuffer 将任意类型的数组转换为 byte 数组
  private RedisTemplate<String, T> redisTemplate;
  private String key;
  private Strategy strategy;

  private RedisBloomFilter(
      RedisTemplate<String, T> redisTemplate,
      String key,
      int numHashFunctions,
      Funnel<? super T> funnel,
      Strategy strategy) {
    checkArgument(numHashFunctions > 0, "numHashFunctions (%s) must be > 0", numHashFunctions);
    checkArgument(
        numHashFunctions <= 255, "numHashFunctions (%s) must be <= 255", numHashFunctions);
    this.redisTemplate = redisTemplate;
    this.key = key;
    this.numHashFunctions = numHashFunctions;
    this.funnel = funnel;
    this.strategy = strategy;
  }

  static <T> RedisBloomFilter<T> create(
      RedisTemplate<String, T> redisTemplate,
      String key,
      Funnel<? super T> funnel,
      long expectedInsertions,
      double fpp,
      Strategy strategy) {
    long numBits = optimalNumOfBits(expectedInsertions, fpp);
    int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
    try {
      return new RedisBloomFilter<T>(redisTemplate, key, numHashFunctions, funnel, strategy);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(
          "Could not create RedisBloomFilter of " + numBits + " bits", e);
    }
  }

  private static long optimalNumOfBits(long n, double p) {
    if (p == 0) {
      p = Double.MIN_VALUE;
    }
    return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
  }

  private static int optimalNumOfHashFunctions(long n, long m) {
    return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
  }

  public boolean put(T value) {
    return strategy.put(redisTemplate, key, value, numHashFunctions, funnel);
  }

  public boolean mightContain(T value) {
    return strategy.mightContain(redisTemplate, key, value, numHashFunctions, funnel);
  }

  interface Strategy extends java.io.Serializable {

    <T> boolean put(
        RedisTemplate<String, T> redisTemplate,
        String key,
        T value,
        int numHashFunctions,
        Funnel<? super T> funnel);

    <T> boolean mightContain(
        RedisTemplate<String, T> redisTemplate,
        String key,
        T value,
        int numHashFunctions,
        Funnel<? super T> funnel);
  }
}
