package org.ylzl.eden.spring.boot.rocketmq.autoconfigure;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.StringUtils;

/**
 * RocketMQ 自动装配过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class RocketMQAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {

	private static final String ROCKETMQ_ENABLED = "rocketmq.enabled";

	private static final String DEFAULT_VALUE = "true";

	private static final String MATCH_CLASS = "org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration";

	private Environment environment;

	@Override
	public void setEnvironment(@NotNull Environment environment) {
		this.environment = environment;
	}

	@Override
	public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
		boolean enabled = Boolean.parseBoolean(environment.getProperty(ROCKETMQ_ENABLED, DEFAULT_VALUE));
		if (!enabled) {
			boolean[] match = new boolean[autoConfigurationClasses.length];
			for (int i = 0; i < autoConfigurationClasses.length; i++) {
				match[i] = StringUtils.isEmpty(autoConfigurationClasses[i]) ||
					!autoConfigurationClasses[i].equals(MATCH_CLASS);
			}
			return match;
		}
		return new boolean[0];
	}
}
