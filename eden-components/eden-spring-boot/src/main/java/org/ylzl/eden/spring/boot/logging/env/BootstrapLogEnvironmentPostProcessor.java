package org.ylzl.eden.spring.boot.logging.env;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.commons.net.IpConfigUtils;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringProperties;
import org.ylzl.eden.spring.framework.logging.MdcConstants;

/**
 * 全局日志环境解析后置处理
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
public class BootstrapLogEnvironmentPostProcessor implements EnvironmentPostProcessor {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		if ((!environment.containsProperty(BootstrapLogProperties.ENABLED)) ||
			Boolean.parseBoolean(StringUtils.trimToEmpty(environment.getProperty(BootstrapLogProperties.ENABLED)))) {
			String appName = StringUtils.trimToEmpty(environment.getProperty(SpringProperties.SPRING_APPLICATION_NAME));
			String profile = StringUtils.trimToEmpty(environment.getProperty(SpringProperties.SPRING_PROFILE_DEFAULT));
			MDC.put(MdcConstants.APP, appName);
			MDC.put(MdcConstants.PROFILE, profile);
			MDC.put(MdcConstants.LOCAL_ADDR, IpConfigUtils.getIpAddress());
		}
	}
}
