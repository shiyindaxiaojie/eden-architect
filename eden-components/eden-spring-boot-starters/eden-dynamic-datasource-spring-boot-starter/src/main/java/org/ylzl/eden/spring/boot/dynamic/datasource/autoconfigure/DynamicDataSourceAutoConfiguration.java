package org.ylzl.eden.spring.boot.dynamic.datasource.autoconfigure;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;

/**
 * 多数据源组合配置（sharding-jdbc、baomidou-dynamic）
 *
 * @author gyl
 * @since 2021-12-28
 */
@ConditionalOnClass(DynamicDataSourceProvider.class)
@AutoConfigureBefore(com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceAutoConfiguration.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration
public class DynamicDataSourceAutoConfiguration {

	public static final String AUTOWIRED_DYNAMIC_ROUTING_DATA_SOURCE = "Autowired DynamicRoutingDataSource";

	private final DynamicDataSourceProperties dynamicDataSourceProperties;

	public DynamicDataSourceAutoConfiguration(DynamicDataSourceProperties dynamicDataSourceProperties) {
		this.dynamicDataSourceProperties = dynamicDataSourceProperties;
	}

	/**
	 * 装配数据源
	 *
	 * @return
	 */
	@Primary
	@Bean
	public DataSource dataSource() {
		log.info(AUTOWIRED_DYNAMIC_ROUTING_DATA_SOURCE);
		DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
		dataSource.setPrimary(dynamicDataSourceProperties.getPrimary());
		dataSource.setStrict(dynamicDataSourceProperties.getStrict());
		dataSource.setStrategy(dynamicDataSourceProperties.getStrategy());
		dataSource.setP6spy(dynamicDataSourceProperties.getP6spy());
		dataSource.setSeata(dynamicDataSourceProperties.getSeata());
		return dataSource;
	}
}
