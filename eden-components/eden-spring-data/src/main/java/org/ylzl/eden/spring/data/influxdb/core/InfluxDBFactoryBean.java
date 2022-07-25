package org.ylzl.eden.spring.data.influxdb.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient.Builder;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class InfluxDBFactoryBean implements FactoryBean<InfluxDB>, InitializingBean, DisposableBean {

	private final InfluxDBConfig influxDBConfig;
	private InfluxDB influxDB;

	@Override
	public InfluxDB getObject() {
		if (influxDB == null) {
			final Builder client = new Builder()
				.connectTimeout(influxDBConfig.getConnectTimeout(), TimeUnit.SECONDS)
				.writeTimeout(influxDBConfig.getWriteTimeout(), TimeUnit.SECONDS)
				.readTimeout(influxDBConfig.getReadTimeout(), TimeUnit.SECONDS);
			log.info("Build influxdb okHttpClient, connectTimeout: {}, writeTimeout: {}, readTimeout: {}.",
				influxDBConfig.getConnectTimeout(), influxDBConfig.getWriteTimeout(), influxDBConfig.getReadTimeout());

			influxDB = InfluxDBFactory.connect(influxDBConfig.getUrl(), influxDBConfig.getUsername(), influxDBConfig.getPassword(), client);
			log.info("Using influxdb '{}' on '{}'", influxDBConfig.getDatabase(), influxDBConfig.getUrl());
			if (influxDBConfig.isGzip()) {
				log.info("Enabled influxdb gzip compression for HTTP requests");
				influxDB.enableGzip();
			}

			if (influxDBConfig.isEnableBatch()) {
				BatchOptions batchOptions = BatchOptions.DEFAULTS
					.actions(influxDBConfig.getBatchAction())
					.flushDuration(influxDBConfig.getFlushDuration())
					.jitterDuration(influxDBConfig.getJitterDuration());
				influxDB.enableBatch(batchOptions);
			}
		}
		return influxDB;
	}

	@Override
	public Class<?> getObjectType() {
		return InfluxDB.class;
	}

	@Override
	public void afterPropertiesSet() {

	}

	@Override
	public void destroy() {
		influxDB.close();
	}
}
