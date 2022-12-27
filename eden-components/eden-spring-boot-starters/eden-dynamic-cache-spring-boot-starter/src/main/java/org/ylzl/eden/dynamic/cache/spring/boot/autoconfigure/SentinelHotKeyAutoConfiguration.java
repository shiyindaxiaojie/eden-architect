package org.ylzl.eden.dynamic.cache.spring.boot.autoconfigure;

import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.config.SentinelConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.apollo.ApolloDataSource;
import com.alibaba.csp.sentinel.datasource.etcd.EtcdConfig;
import com.alibaba.csp.sentinel.datasource.etcd.EtcdDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.commons.lang.StringUtils;
import org.ylzl.eden.dynamic.cache.config.CacheConfig;
import org.ylzl.eden.dynamic.cache.spring.boot.env.CacheProperties;
import org.ylzl.eden.spring.boot.bootstrap.constant.Conditions;

import java.util.List;
import java.util.Properties;

/**
 * SentinelHotKey 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.13
 */
@ConditionalOnExpression("${spring.cloud.sentinel.enabled:true}")
@ConditionalOnProperty(
	prefix = CacheProperties.PREFIX + ".dynamic.hotkey.sentinel",
	name = Conditions.ENABLED,
	havingValue = Conditions.TRUE,
	matchIfMissing = true
)
@ConditionalOnClass(SphU.class)
@RequiredArgsConstructor
@Slf4j
@Configuration(proxyBeanMethods = false)
public class SentinelHotKeyAutoConfiguration implements InitializingBean {

	private static final String AUTOWIRED_SENTINEL_KEY = "Autowired SentinelHotKey";

	private final CacheProperties cacheProperties;

	@Value("${project.name:${spring.application.name:}}")
	private String applicationName;

	@Override
	public void afterPropertiesSet() {
		log.debug(AUTOWIRED_SENTINEL_KEY);
		CacheConfig.HotKey.Sentinel sentinelConfig = cacheProperties.getDynamic().getHotKey().getSentinel();
		this.initSentinel(sentinelConfig);
		this.registerDataSource(sentinelConfig);
	}

	private void initSentinel(CacheConfig.HotKey.Sentinel sentinelConfig) {
		if (StringUtils.isEmpty(System.getProperty(SentinelConfig.APP_NAME_PROP_KEY)) &&
			StringUtils.isNotBlank(applicationName)) {
			System.setProperty(SentinelConfig.APP_NAME_PROP_KEY, applicationName);
		}
		if (StringUtils.isEmpty(System.getProperty(TransportConfig.SERVER_PORT)) &&
			StringUtils.isNotBlank(sentinelConfig.getTransport().getPort())) {
			System.setProperty(TransportConfig.SERVER_PORT, sentinelConfig.getTransport().getPort());
		}
		if (StringUtils.isEmpty(System.getProperty(TransportConfig.CONSOLE_SERVER)) &&
			StringUtils.isNotBlank(sentinelConfig.getTransport().getDashboard())) {
			System.setProperty(TransportConfig.CONSOLE_SERVER, sentinelConfig.getTransport().getDashboard());
		}
	}

	private void registerDataSource(CacheConfig.HotKey.Sentinel sentinelConfig) {
		if (sentinelConfig.getDatasource().getNacos() != null) {
			registerNacosDataSource(sentinelConfig);
		} else if (sentinelConfig.getDatasource().getApollo() != null) {
			registerApolloDataSource(sentinelConfig);
		} else if (sentinelConfig.getDatasource().getZk() != null) {
			registerZookeeperDataSource(sentinelConfig);
		} else if (sentinelConfig.getDatasource().getEtcd() != null) {
			registerEtcdDataSource(sentinelConfig);
		}
	}

	private void registerNacosDataSource(CacheConfig.HotKey.Sentinel sentinelConfig) {
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.SERVER_ADDR, sentinelConfig.getDatasource().getNacos().getServerAddr());
		properties.put(PropertyKeyConst.NAMESPACE, sentinelConfig.getDatasource().getNacos().getNamespace());
		ReadableDataSource<String, List<ParamFlowRule>> dataSource = new NacosDataSource<>(properties,
			sentinelConfig.getDatasource().getNacos().getGroupId(),
			sentinelConfig.getDatasource().getNacos().getDataId(),
			source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
		ParamFlowRuleManager.register2Property(dataSource.getProperty());
	}

	private void registerApolloDataSource(CacheConfig.HotKey.Sentinel sentinelConfig) {
		System.setProperty("app.id", applicationName);
		System.setProperty("apollo.meta", sentinelConfig.getDatasource().getApollo().getServerAddr());
		ReadableDataSource<String, List<ParamFlowRule>> dataSource = new ApolloDataSource<>(
			sentinelConfig.getDatasource().getApollo().getNamespaceName(),
			sentinelConfig.getDatasource().getApollo().getFlowRulesKey(),
			sentinelConfig.getDatasource().getApollo().getDefaultFlowRuleValue(),
			source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
		ParamFlowRuleManager.register2Property(dataSource.getProperty());
	}

	private void registerZookeeperDataSource(CacheConfig.HotKey.Sentinel sentinelConfig) {
		ReadableDataSource<String, List<ParamFlowRule>> dataSource = new ZookeeperDataSource<>(
			sentinelConfig.getDatasource().getZk().getServerAddr(),
			sentinelConfig.getDatasource().getZk().getGroupId(),
			sentinelConfig.getDatasource().getZk().getDataId(),
			source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
		ParamFlowRuleManager.register2Property(dataSource.getProperty());
	}

	private void registerEtcdDataSource(CacheConfig.HotKey.Sentinel sentinelConfig) {
		SentinelConfig.setConfig(EtcdConfig.END_POINTS, sentinelConfig.getDatasource().getEtcd().getEndPoints());
		SentinelConfig.setConfig(EtcdConfig.USER, sentinelConfig.getDatasource().getEtcd().getUser());
		SentinelConfig.setConfig(EtcdConfig.PASSWORD, sentinelConfig.getDatasource().getEtcd().getPassword());
		SentinelConfig.setConfig(EtcdConfig.CHARSET, sentinelConfig.getDatasource().getEtcd().getCharset());
		SentinelConfig.setConfig(EtcdConfig.AUTH_ENABLE, String.valueOf(sentinelConfig.getDatasource().getEtcd().isAuth()));
		ReadableDataSource<String, List<ParamFlowRule>> dataSource = new EtcdDataSource<>(
			sentinelConfig.getDatasource().getEtcd().getRuleKey(),
			source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {}));
		ParamFlowRuleManager.register2Property(dataSource.getProperty());
	}
}
