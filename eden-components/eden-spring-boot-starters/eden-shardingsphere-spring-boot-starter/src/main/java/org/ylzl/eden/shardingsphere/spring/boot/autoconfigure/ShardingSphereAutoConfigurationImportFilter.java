package org.ylzl.eden.shardingsphere.spring.boot.autoconfigure;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * ShardingSphere 自动装配过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ShardingSphereAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {

	private static final String MATCH_KEY = "spring.shardingsphere.enabled";

	private static final String DEFAULT_VALUE = "true";

	private static final String[] IGNORE_CLASSES = {
		"org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration",
		"org.apache.shardingsphere.sharding.spring.boot.ShardingRuleSpringBootConfiguration",
		"org.apache.shardingsphere.shadow.spring.boot.ShadowRuleSpringBootConfiguration",
		"org.apache.shardingsphere.parser.spring.boot.SQLParserRuleSpringBootConfiguration",
		"org.apache.shardingsphere.sqltranslator.spring.boot.SQLTranslatorRuleSpringBootConfiguration",
		"org.apache.shardingsphere.encrypt.spring.boot.EncryptRuleSpringBootConfiguration",
		"org.apache.shardingsphere.readwritesplitting.spring.boot.ReadwriteSplittingRuleSpringbootConfiguration",
		"org.apache.shardingsphere.dbdiscovery.spring.boot.DatabaseDiscoveryRuleSpringbootConfiguration"
	};

	private Environment environment;

	@Override
	public void setEnvironment(@NotNull Environment environment) {
		this.environment = environment;
	}

	@Override
	public boolean[] match(String[] autoConfigurationClasses, AutoConfigurationMetadata autoConfigurationMetadata) {
		boolean disabled = !Boolean.parseBoolean(environment.getProperty(MATCH_KEY, DEFAULT_VALUE));
		boolean[] match = new boolean[autoConfigurationClasses.length];
		for (int i = 0; i < autoConfigurationClasses.length; i++) {
			int index = i;
			match[i] = !disabled || Arrays.stream(IGNORE_CLASSES).noneMatch(e -> e.equals(autoConfigurationClasses[index]));
		}
		return match;
	}
}
