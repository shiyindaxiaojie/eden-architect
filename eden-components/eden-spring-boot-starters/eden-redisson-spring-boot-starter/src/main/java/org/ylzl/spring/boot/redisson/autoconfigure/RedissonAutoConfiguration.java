package org.ylzl.spring.boot.redisson.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Sentinel;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.ReflectionUtils;
import org.ylzl.spring.boot.redisson.env.FixedRedissonProperties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Redisson 自动配置
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
@ConditionalOnProperty(value = "redisson.enabled", matchIfMissing = true)
@EnableConfigurationProperties(FixedRedissonProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class RedissonAutoConfiguration {

	private static final String REDIS_PROTOCOL_PREFIX = "redis://";

	private static final String REDISS_PROTOCOL_PREFIX = "rediss://";

	private final RedisProperties redisProperties;

	private final FixedRedissonProperties redissonProperties;

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonReactiveClient.class)
	public RedissonReactiveClient redissonReactive(RedissonClient redisson) {
		return redisson.reactive();
	}

	@Bean
	@Lazy
	@ConditionalOnMissingBean(RedissonRxClient.class)
	public RedissonRxClient redissonRxJava(RedissonClient redisson) {
		return redisson.rxJava();
	}

	@Bean
	@ConditionalOnMissingBean(RedisConnectionFactory.class)
	public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
		return new RedissonConnectionFactory(redisson);
	}

	@ConditionalOnMissingBean(RedissonClient.class)
	@Bean(destroyMethod = "shutdown")
	public RedissonClient redissonClient() {
		Method clusterMethod = ReflectionUtils.findMethod(RedisProperties.class, "getCluster");
		Method usernameMethod = ReflectionUtils.findMethod(RedisProperties.class, "getUsername");
		Method timeoutMethod = ReflectionUtils.findMethod(RedisProperties.class, "getTimeout");
		Object timeoutValue = ReflectionUtils.invokeMethod(timeoutMethod, redisProperties);
		int timeout;
		if (null == timeoutValue) {
			timeout = 10000;
		} else if (!(timeoutValue instanceof Integer)) {
			Method millisMethod = ReflectionUtils.findMethod(timeoutValue.getClass(), "toMillis");
			timeout = ((Long) ReflectionUtils.invokeMethod(millisMethod, timeoutValue)).intValue();
		} else {
			timeout = (Integer) timeoutValue;
		}

		String username = null;
		if (usernameMethod != null) {
			username = (String) ReflectionUtils.invokeMethod(usernameMethod, redisProperties);
		}

		Config config = new Config();
		if (redisProperties.getSentinel() != null) {
			// 哨兵
			Method nodesMethod = ReflectionUtils.findMethod(Sentinel.class, "getNodes");
			Object nodesValue = ReflectionUtils.invokeMethod(nodesMethod, redisProperties.getSentinel());

			String[] nodes;
			if (nodesValue instanceof String) {
				nodes = convert(Arrays.asList(((String) nodesValue).split(",")));
			} else {
				nodes = convert((List<String>) nodesValue);
			}

			config.useSentinelServers()
				.setMasterName(redisProperties.getSentinel().getMaster())
				.addSentinelAddress(nodes)
				.setDatabase(redisProperties.getDatabase())
				.setConnectTimeout(timeout)
				.setUsername(username)
				.setPassword(redisProperties.getPassword())
				// 补充
				.setTimeout(redissonProperties.getTimeout());
		} else if (clusterMethod != null && ReflectionUtils.invokeMethod(clusterMethod, redisProperties) != null) {
			// 集群
			Object clusterObject = ReflectionUtils.invokeMethod(clusterMethod, redisProperties);
			Method nodesMethod = ReflectionUtils.findMethod(clusterObject.getClass(), "getNodes");
			List<String> nodesObject = (List) ReflectionUtils.invokeMethod(nodesMethod, clusterObject);

			String[] nodes = convert(nodesObject);

			config.useClusterServers()
				.addNodeAddress(nodes)
				.setConnectTimeout(timeout)
				.setUsername(username)
				.setPassword(redisProperties.getPassword())
				// 补充
				.setTimeout(redissonProperties.getTimeout());
		} else {
			// 单机
			String prefix = REDIS_PROTOCOL_PREFIX;
			Method method = ReflectionUtils.findMethod(RedisProperties.class, "isSsl");
			if (method != null && (Boolean) ReflectionUtils.invokeMethod(method, redisProperties)) {
				prefix = REDISS_PROTOCOL_PREFIX;
			}

			config.useSingleServer()
				.setAddress(prefix + redisProperties.getHost() + ":" + redisProperties.getPort())
				.setConnectTimeout(timeout)
				.setDatabase(redisProperties.getDatabase())
				.setUsername(username)
				.setPassword(redisProperties.getPassword())
				// 补充
				.setTimeout(redissonProperties.getTimeout())
				.setConnectionPoolSize(redissonProperties.getConnectionPoolSize())
				.setConnectionMinimumIdleSize(redissonProperties.getConnectionMinimumIdleSize());
		}

		return Redisson.create(config);
	}

	private String[] convert(List<String> nodesObject) {
		List<String> nodes = new ArrayList<>(nodesObject.size());
		for (String node : nodesObject) {
			if (!node.startsWith(REDIS_PROTOCOL_PREFIX) && !node.startsWith(REDISS_PROTOCOL_PREFIX)) {
				nodes.add(REDIS_PROTOCOL_PREFIX + node);
			} else {
				nodes.add(node);
			}
		}
		return nodes.toArray(new String[nodes.size()]);
	}
}
