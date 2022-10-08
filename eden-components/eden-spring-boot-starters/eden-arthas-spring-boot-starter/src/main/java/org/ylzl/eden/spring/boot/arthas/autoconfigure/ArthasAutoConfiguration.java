package org.ylzl.eden.spring.boot.arthas.autoconfigure;

import com.alibaba.arthas.spring.ArthasConfiguration;
import com.alibaba.arthas.spring.ArthasProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.ylzl.eden.spring.boot.arthas.env.SpringArthasProperties;
import org.ylzl.eden.spring.boot.arthas.listener.ArthasEnvironmentChangeListener;

import java.util.Map;

/**
 * Arthas 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	name = {"spring.arthas.enabled"},
	matchIfMissing = true
)
@AutoConfigureAfter(ArthasConfiguration.class)
@EnableConfigurationProperties(SpringArthasProperties.class)
@RequiredArgsConstructor
@Slf4j
@Configuration
public class ArthasAutoConfiguration {

	private static final String ARTHAS_CONFIG_MAP = "arthasConfigMap";

	private final Environment environment;

	private final ApplicationContext applicationContext;

	private final ArthasProperties arthasProperties;

	private final SpringArthasProperties springArthasProperties;

	@Bean
	public ArthasEnvironmentChangeListener arthasEnvironmentChangeListener(
		@Autowired @Qualifier(ARTHAS_CONFIG_MAP) Map<String, String> arthasConfigMap) {
		return new ArthasEnvironmentChangeListener(environment, applicationContext,
			arthasProperties, springArthasProperties, arthasConfigMap);
	}
}
