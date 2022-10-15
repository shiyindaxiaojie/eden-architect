package org.ylzl.eden.spring.boot.elasticsearch.autoconfigure;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.AutoConfigurationImportFilter;
import org.springframework.boot.autoconfigure.AutoConfigurationMetadata;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Elasticsearch 自动装配过滤
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
public class ElasticsearchAutoConfigurationImportFilter implements AutoConfigurationImportFilter, EnvironmentAware {

	private static final String ELASTICSEARCH_ENABLED = "spring.elasticsearch.enabled";

	private static final String DEFAULT_VALUE = "true";

	private static final String[] MATCH_CLASSES = {
		"org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRepositoriesAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration"
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
