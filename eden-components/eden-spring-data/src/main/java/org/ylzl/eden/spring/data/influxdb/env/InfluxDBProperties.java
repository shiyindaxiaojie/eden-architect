package org.ylzl.eden.spring.data.influxdb.env;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO
 *
 * @author <a href="mailto:guoyuanlu@puyiwm.com">gyl</a>
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.influxdb")
public class InfluxDBProperties {

	private String url;

	private String username;

	private String password;

	private String database;

	private String retentionPolicy = "autogen";

	private int connectTimeout = 10;

	private int readTimeout = 30;

	private int writeTimeout = 10;

	private boolean gzip = false;

	private boolean enableBatch = false;

	private int batchAction = 2000;

	private int flushDuration = 1000;

	private int jitterDuration = 500;

	private int udpPort = 8089;
}
