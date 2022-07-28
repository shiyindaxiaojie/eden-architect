package org.ylzl.eden.spring.data.influxdb.autoconfigure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.impl.InfluxDBMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylzl.eden.spring.data.influxdb.core.InfluxDBConfig;
import org.ylzl.eden.spring.data.influxdb.core.InfluxDBFactoryBean;
import org.ylzl.eden.spring.data.influxdb.core.InfluxDBTemplate;
import org.ylzl.eden.spring.data.influxdb.env.InfluxDBProperties;

/**
 * InfluxDB 自动装配
 *
 * @author <a href="mailto:shiyindaxiaojie@gmail.com">gyl</a>
 * @since 2.4.x
 */
@RequiredArgsConstructor
@Slf4j
@EnableConfigurationProperties(InfluxDBProperties.class)
@Configuration
public class InfluxDBAutoConfiguration {

	private final InfluxDBProperties properties;


	@ConditionalOnMissingBean
	@Bean
	public InfluxDBFactoryBean influxDBFactory() {
		return new InfluxDBFactoryBean(InfluxDBConfig.builder()
			.url(properties.getUrl())
			.database(properties.getDatabase())
			.username(properties.getUsername())
			.password(properties.getPassword())
			.connectTimeout(properties.getConnectTimeout())
			.readTimeout(properties.getReadTimeout())
			.writeTimeout(properties.getWriteTimeout())
			.flushDuration(properties.getFlushDuration())
			.jitterDuration(properties.getJitterDuration())
			.batchAction(properties.getBatchAction())
			.udpPort(properties.getUdpPort()).build());
	}

	@ConditionalOnMissingBean
	@Bean
	public InfluxDBMapper influxDBMapper(InfluxDB influxDB) {
		influxDB.setLogLevel(InfluxDB.LogLevel.BASIC);
		return new InfluxDBMapper(influxDB);
	}

	@ConditionalOnMissingBean
	@Bean
	public InfluxDBTemplate influxDBTemplate(InfluxDB influxDB, InfluxDBMapper influxDBMapper) {
		return new InfluxDBTemplate(influxDB, influxDBMapper);
	}
}
