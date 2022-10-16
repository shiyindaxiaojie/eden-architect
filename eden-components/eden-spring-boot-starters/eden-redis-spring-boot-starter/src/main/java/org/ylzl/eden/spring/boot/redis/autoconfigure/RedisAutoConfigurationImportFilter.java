package org.ylzl.eden.spring.boot.redis.autoconfigure;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Redis 自动装配过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class RedisAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {

	private static final String ELASTICSEARCH_ENABLED = "spring.redis.enabled";

	private static final String DEFAULT_VALUE = "true";

	private static final String[] MATCH_CLASSES = {
		"org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration"
	};

	private Environment environment;

	@Override
	public void setEnvironment(@NotNull Environment environment) {
		this.environment = environment;
	}

	@Override
	public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
		boolean enabled = Boolean.parseBoolean(environment.getProperty(ELASTICSEARCH_ENABLED, DEFAULT_VALUE));
		if (!enabled) {
			boolean[] match = new boolean[autoConfigurationClasses.length];
			for (int i = 0; i < autoConfigurationClasses.length; i++) {
				int index = i;
				match[i] = StringUtils.isEmpty(autoConfigurationClasses[i]) ||
					Arrays.stream(MATCH_CLASSES).anyMatch(e -> autoConfigurationClasses[index].equals(e));
			}
			return match;
		}
		return new boolean[0];
	}
}
