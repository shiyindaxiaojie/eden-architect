package org.ylzl.eden.spring.boot.cat.autoconfigure;

import com.dianping.cat.Cat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.ylzl.eden.commons.lang.StringConstants;
import org.ylzl.eden.spring.boot.cat.env.CatProperties;
import org.ylzl.eden.spring.framework.bootstrap.constant.SpringPropertiesConstants;
import org.ylzl.eden.spring.framework.error.util.AssertEnhancer;
import org.ylzl.eden.spring.integration.cat.annotations.CatAnnotationProcessorRegister;

/**
 * CAT 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@Import(CatAnnotationProcessorRegister.class)
@ConditionalOnProperty(prefix = CatProperties.PREFIX, name = "enabled", matchIfMissing = true)
@ConditionalOnClass(Cat.class)
@EnableConfigurationProperties(CatProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class CatAutoConfiguration implements InitializingBean {

	public static final String CAT_HOME = "CAT_HOME";

	private final CatProperties catProperties;

	private final Environment environment;

	@Override
	public void afterPropertiesSet() {
		log.debug("Initializing cat client");
		System.setProperty(CAT_HOME, catProperties.getHome());

		String servers = catProperties.getServers();
		AssertEnhancer.notNull(servers, "cat servers is not null");

		String domain;
		if (StringUtils.isBlank(catProperties.getDomain())) {
			domain = environment.getProperty(SpringPropertiesConstants.SPRING_APPLICATION_NAME);
		} else {
			domain = catProperties.getDomain();
		}

		Cat.initializeByDomain(domain,
			catProperties.getTcpPort(),
			catProperties.getHttpPort(),
			servers.split(StringConstants.COMMA));
	}
}
