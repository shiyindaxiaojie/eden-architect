package org.ylzl.eden.dynamic.cache.spring.boot.autoconfigure;

import com.jd.platform.hotkey.client.ClientStarter;
import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.spring.boot.env.CacheProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

/**
 * JdHotKey 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnProperty(
	prefix = CacheProperties.PREFIX + ".hotkey.jd",
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnClass(JdHotKeyStore.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class JdHotKeyAutoConfiguration implements InitializingBean {

	private static final String AUTOWIRED_JD_HOT_KEY = "Autowired JdHotKey";

	private final CacheProperties cacheProperties;

	@Override
	public void afterPropertiesSet() {
		log.debug(AUTOWIRED_JD_HOT_KEY);
		CacheConfig.HotKey.JD jd = cacheProperties.getHotKey().getJd();
		ClientStarter.Builder builder = new ClientStarter.Builder();
		ClientStarter starter = builder.setAppName(jd.getAppName()).setEtcdServer(jd.getEtcdServer()).build();
		starter.startPipeline();
	}
}
