package org.ylzl.eden.spring.framework.core.bind;

import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * 配置属性绑定助手
 *
 * <p>从 Spring Boot 1.X 升级到 2.X
 *
 * <ul>
 *   <li>org.springframework.boot.bind.RelaxedPropertyResolver 变更为 {@link Binder}
 * </ul>
 *
 * @author gyl
 * @since 2.0.0
 */
public class SpringBinderHelper {

	private final Environment env;

	public SpringBinderHelper(Environment env) {
		this.env = env;
	}

	public <T> T bind(String prefix, Class<T> type) {
		Binder binder = Binder.get(env);
		BindResult<T> bindResult = binder.bind(prefix, type);
		return bindResult.get();
	}

	public Properties getProperties(String prefix) {
		Iterable<ConfigurationPropertySource> sources = ConfigurationPropertySources.get(env);
		Binder binder = new Binder(sources);
		BindResult<Properties> bindResult = binder.bind(prefix, Properties.class);
		return bindResult.get();
	}
}
